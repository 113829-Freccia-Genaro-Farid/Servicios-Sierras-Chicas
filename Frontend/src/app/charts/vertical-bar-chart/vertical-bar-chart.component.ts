import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import {
  ApexAxisChartSeries,
  ApexChart,
  ApexDataLabels,
  ApexGrid,
  ApexStroke,
  ApexTitleSubtitle,
  ApexXAxis,
  ApexYAxis,
  ChartComponent
} from "ng-apexcharts";
import { ProfesionistasService } from "../../services/profesionistasService/profesionistas.service";
import { Subscription } from "rxjs";
import { UsuarioService } from "../../services/usuariosService/usuario.service";
import { Anuncio } from "../../models/anuncio";

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  dataLabels: ApexDataLabels;
  grid: ApexGrid;
  stroke: ApexStroke;
  title: ApexTitleSubtitle;
  yaxis: ApexYAxis;
};

export type MesAnio = {
  anio: number;
  mes: string;
  clicks: number;
};

@Component({
  selector: 'vertical-bar-chart',
  templateUrl: './vertical-bar-chart.component.html',
  styleUrls: ['./vertical-bar-chart.component.css']
})
export class VerticalBarChartComponent implements OnInit, OnDestroy {
  @ViewChild("chartVert") chart: ChartComponent | undefined;
  private subscription: Subscription | undefined;
  public chartOptions: Partial<ChartOptions>;
  profesionistaId: number = 0;
  anunciosStats: Anuncio[] = [];
  mesAnios: MesAnio[] = [];
  availableYears: number[] = [];
  selectedYear: number | undefined;

  constructor(private profesionistaService: ProfesionistasService,
              private userService: UsuarioService) {
    this.chartOptions = {
      series: [{
        name: "Clicks",
        data: []
      }],
      chart: {
        height: 350,
        type: "line",
        zoom: {
          enabled: false
        }
      },
      dataLabels: {
        enabled: false
      },
      stroke: {
        curve: "straight"
      },
      grid: {
        row: {
          colors: ["#f3f3f3", "transparent"],
          opacity: 0.5
        }
      },
      xaxis: {
        categories: [],
        title: {
          text: 'Mes'
        }
      },
      yaxis: {
        title: {
          text: 'Cantidad de Clicks'
        }
      }
    };
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  ngOnInit(): void {
    this.subscription = new Subscription();
    this.subscription.add(
      this.profesionistaService.getProfesionistaByUserEmail(this.userService.getUsuarioLogueado().email).subscribe({
        next: (response) => {
          this.profesionistaId = response.id;
          this.cargarDatosAnuncios();

          setTimeout(()=> (window as any).dispatchEvent(new Event('resize')), 1)
        }
      })
    );
  }

  onYearChange(event: any): void {
    this.selectedYear = event.value;
    this.updateChartData();
  }

  private cargarDatosAnuncios() {
    this.subscription?.add(
      this.profesionistaService.getClicksAnuncio(this.profesionistaId).subscribe({
        next: (response) => {
          this.anunciosStats = response;
          this.mesAnios = this.anunciosStats.map(a => ({
            anio: +a.anio,
            mes: new Date(a.anio, a.mes - 1).toLocaleDateString('es-ES', { month: "long" }).charAt(0).toUpperCase() + new Date(a.anio, a.mes - 1).toLocaleDateString('es-ES', { month: "long" }).slice(1),
            clicks: a.cantidadClicks
          }));

          this.availableYears = Array.from(new Set(this.mesAnios.map(usuario => usuario.anio))).sort((a, b) => b - a);
          this.updateChartData();
        }
      })
    );
  }

  private updateChartData(): void {
    if (this.selectedYear) {
      const filteredData = this.mesAnios.filter(m => m.anio === this.selectedYear);
      const categories = filteredData.map(m => m.mes + '/' + m.anio);
      const data = filteredData.map(m => m.clicks);

      this.chartOptions.xaxis!.categories = categories;
      this.chartOptions.series![0].data = data;

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
    }
  }
}
