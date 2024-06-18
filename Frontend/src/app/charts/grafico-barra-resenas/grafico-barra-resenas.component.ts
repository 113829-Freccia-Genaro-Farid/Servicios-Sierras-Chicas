import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {
  ApexAxisChartSeries,
  ApexChart,
  ApexDataLabels,
  ApexPlotOptions, ApexTheme,
  ApexTitleSubtitle, ApexTooltip,
  ApexXAxis, ApexYAxis
} from "ng-apexcharts";
import {Subscription} from "rxjs";
import {ReseniaStats} from "../../models/resenia-stats";
import {ProfesionistasService} from "../../services/profesionistasService/profesionistas.service";

export type ChartOptions = {
  title: ApexTitleSubtitle;
  subtitle: ApexTitleSubtitle;
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  plotOptions: ApexPlotOptions;
  xaxis: ApexXAxis;
  yaxis: ApexYAxis;
  tooltip: ApexTooltip;
  theme: ApexTheme;
  colors: string[];
};
@Component({
  selector: 'grafico-barra-resenas',
  templateUrl: './grafico-barra-resenas.component.html',
  styleUrls: ['./grafico-barra-resenas.component.css']
})
export class GraficoBarraResenasComponent implements OnInit, OnDestroy{
  @Input() idProfesionista: number = 0;
  private subscription: Subscription | undefined;
  reseniaStats: ReseniaStats = {} as ReseniaStats;
  public chartOptions: Partial<ChartOptions>;

  constructor(private profesionistaService: ProfesionistasService) {
    this.chartOptions = {
      title: {
        text: "Estadisticas de reseñas",
        align: "center",
        floating: true
      },
      series: [],
      chart: {
        type: 'bar',
        height: 170,
        offsetY: 40,
        background:'white',
        toolbar:{
          show:false
        }
      },
      plotOptions: {
        bar: {
          horizontal: true
        }
      },
      dataLabels: {
        enabled: true,
        formatter: function (val) {
          return val === 0 ? '' : val.toString();
        },
        style:{
          colors:["#000"]
        }
      },
      xaxis: {
        categories: [
          'Una Estrella',
          'Dos Estrellas',
          'Tres Estrellas',
          'Cuatro Estrellas',
          'Cinco Estrellas'
        ],
        tickAmount: 8,
        labels: {
          style: {
            colors: ['#000000']
          },
          formatter: function (value) {
            if (+value % 1 === 0) {
              return value.toString();
            } else {
              return '';
            }
          }
        },
      },
      colors: ['#fff400'],
      tooltip: {
        enabled: false,
      }
    };
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  ngOnInit(): void {
    this.subscription = new Subscription();

    if (this.idProfesionista != 0) {
      this.subscription.add(
        this.profesionistaService.getReseniasStatsByProfesionista(this.idProfesionista).subscribe({
          next: (response: ReseniaStats) => {
            if (response) {
              this.reseniaStats = response;
              this.updateChartOptions();
            }
          },
          error: () => {
            alert('Error al encontrar las estadísticas de reseñas');
          }
        })
      );
    }
  }

  updateChartOptions(): void {
    this.chartOptions.title = {
      text: "Estadisticas de reseñas - Total: "+this.reseniaStats.totalResenias,
      align: "center",
      floating: true
    }
    this.chartOptions.series = [
      {
        name: 'Reseñas',
        data: [
          this.reseniaStats.unaEstrella,
          this.reseniaStats.dosEstrella,
          this.reseniaStats.tresEstrella,
          this.reseniaStats.cuatroEstrella,
          this.reseniaStats.cincoEstrella
        ]
      }
    ];

    setTimeout(()=> (window as any).dispatchEvent(new Event('resize')), 1)
  }

  recargarDatos(){
    this.subscription?.add(
      this.profesionistaService.getReseniasStatsByProfesionista(this.idProfesionista).subscribe({
        next: (response: ReseniaStats) => {
          if (response) {
            this.reseniaStats = response;
            this.updateChartOptions();
          }
        },
        error: () => {
          alert('Error al encontrar las estadísticas de reseñas');
        }
      })
    );
  }
}

