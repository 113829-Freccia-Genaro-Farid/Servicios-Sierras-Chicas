import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import {
  ApexAxisChartSeries,
  ApexChart,
  ChartComponent,
  ApexDataLabels,
  ApexPlotOptions,
  ApexYAxis,
  ApexLegend,
  ApexStroke,
  ApexXAxis,
  ApexFill,
  ApexTooltip
} from "ng-apexcharts";
import { Subscription } from "rxjs";
import { Turno } from "../../models/turno";
import { TurnosService } from "../../services/turnosService/turnos.service";
import { ProfesionistasService } from "../../services/profesionistasService/profesionistas.service";
import { UsuarioService } from "../../services/usuariosService/usuario.service";

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  plotOptions: ApexPlotOptions;
  yaxis: ApexYAxis;
  xaxis: ApexXAxis;
  fill: ApexFill;
  tooltip: ApexTooltip;
  stroke: ApexStroke;
  legend: ApexLegend;
};

export type TurnosMes = {
  numMes: number;
  mes: string;
  anio: number;
  cant: number;
}

@Component({
  selector: 'grafico-turnos-mes',
  templateUrl: './grafico-turnos-mes.component.html',
  styleUrls: ['./grafico-turnos-mes.component.css']
})
export class GraficoTurnosMesComponent implements OnInit, OnDestroy {
  @ViewChild("graficoTurnos") chart: ChartComponent | undefined;
  public chartOptions: Partial<ChartOptions>;
  private subscription: Subscription | undefined;
  turnos: Turno[] = [];
  t: TurnosMes[] = [];
  availableYears: number[] = [];

  constructor(private turnosService: TurnosService,
              private profesionistaService: ProfesionistasService,
              private userService: UsuarioService) {
    this.chartOptions = {
      series: [
        {
          name: "Turnos",
          data: []
        }
      ],
      chart: {
        type: "bar",
        height: 350
      },
      plotOptions: {
        bar: {
          horizontal: false,
          columnWidth: "55%"
        }
      },
      dataLabels: {
        enabled: false
      },
      stroke: {
        show: true,
        width: 2,
        colors: ["transparent"]
      },
      xaxis: {
        categories: [],
        title: {
          text: "Mes"
        }
      },
      yaxis: {
        title: {
          text: "Cantidad de Turnos"
        }
      },
      fill: {
        opacity: 1
      }
    };
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  ngOnInit(): void {
    this.subscription = new Subscription();
    this.getTurnos();
  }

  getTurnos() {
    this.subscription?.add(
      this.profesionistaService.getProfesionistaByUserEmail(this.userService.getUsuarioLogueado().email).subscribe({
        next: (prof) => {
          this.turnosService.getTurnosByProfesionista(prof.id).subscribe({
            next: (response) => {
              this.turnos = response;
              this.mapearTurnos();
            },
            error: () => {
              alert("Error al cargar los turnos");
            }
          })
        }
      })
    )
  }

  mapearTurnos() {
    const turnosPorMesAnio: { [key: string]: number } = {};

    this.turnos.forEach(turno => {
      const fecha = new Date(turno.fechaTurno);
      const mes = fecha.getMonth();
      const anio = fecha.getFullYear();
      const key = `${anio}-${mes}`;

      if (turnosPorMesAnio[key]) {
        turnosPorMesAnio[key]++;
      } else {
        turnosPorMesAnio[key] = 1;
      }
    });

    this.t = Object.keys(turnosPorMesAnio).map(key => {
      const [anio, mes] = key.split('-').map(Number);
      return {
        numMes: mes,
        mes: this.getNombreMes(mes),
        anio: anio,
        cant: turnosPorMesAnio[key]
      };
    });

    // Ordenar por año y luego por mes
    this.t.sort((a, b) => (a.anio - b.anio) || (a.numMes - b.numMes));

    // Obtener años disponibles
    this.availableYears = Array.from(new Set(this.t.map(item => item.anio))).sort((a, b) => b - a);

    // Actualizar datos del gráfico
    //this.updateChartData();
  }

  private updateChartData(): void {
    if (this.chart && this.chartOptions.xaxis && this.chartOptions.series) {
      const categories = this.t.map(item => `${item.mes}/${item.anio}`);
      const data = this.t.map(item => item.cant);

      this.chartOptions.xaxis.categories = categories;
      this.chartOptions.series[0].data = data;

      this.chart?.updateOptions({
        xaxis: {
          categories: categories,
          title: {
            text: 'Mes'
          }
        },
        series: [{
          data: data
        }]
      });

      setTimeout(() => (window as any).dispatchEvent(new Event('resize')), 1);
    }
  }

  private getNombreMes(mes: number): string {
    const meses = ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"];
    return meses[mes];
  }

  onYearChange(selectedYear: number): void {
    // Reiniciar los datos originales del gráfico
    this.t = [];
    this.updateChartData();

    // Filtrar los datos por el año seleccionado
    const filteredData = this.turnos.filter(turno => {
      const fecha = new Date(turno.fechaTurno);
      return fecha.getFullYear() === selectedYear;
    });

    // Mapear y actualizar los datos filtrados
    const turnosPorMesAnio: { [key: string]: number } = {};

    filteredData.forEach(turno => {
      const fecha = new Date(turno.fechaTurno);
      const mes = fecha.getMonth();
      const anio = fecha.getFullYear();
      const key = `${anio}-${mes}`;

      if (turnosPorMesAnio[key]) {
        turnosPorMesAnio[key]++;
      } else {
        turnosPorMesAnio[key] = 1;
      }
    });

    this.t = Object.keys(turnosPorMesAnio).map(key => {
      const [anio, mes] = key.split('-').map(Number);
      return {
        numMes: mes,
        mes: this.getNombreMes(mes),
        anio: anio,
        cant: turnosPorMesAnio[key]
      };
    });

    // Ordenar por año y luego por mes
    this.t.sort((a, b) => (a.anio - b.anio) || (a.numMes - b.numMes));

    // Actualizar datos del gráfico con los datos filtrados
    this.updateChartData();
  }
}
