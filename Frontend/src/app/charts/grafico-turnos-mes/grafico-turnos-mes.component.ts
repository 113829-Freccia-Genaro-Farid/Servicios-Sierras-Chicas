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
        categories: [
          "Ene", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dic"
        ],
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

    // Actualizar datos del gráfico
    this.chartOptions.series = [{
      name: "Turnos",
      data: this.t.map(item => item.cant)
    }];
    this.chartOptions.xaxis = {
      categories: this.t.map(item => `${item.mes}/${item.anio}`)
    };
  }

  private getNombreMes(mes: number): string {
    const meses = ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"];
    return meses[mes];
  }
}
