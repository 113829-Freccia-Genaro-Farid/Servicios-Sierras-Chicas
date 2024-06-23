import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {
  ApexAxisChartSeries,
  ApexChart,
  ApexDataLabels,
  ApexGrid,
  ApexPlotOptions, ApexStroke, ApexTheme,
  ApexTitleSubtitle, ApexTooltip, ApexXAxis, ApexYAxis,
  ChartComponent
} from "ng-apexcharts";
import {ProfesionistasService} from "../../services/profesionistasService/profesionistas.service";
import {Subscription} from "rxjs";
import {UsuarioService} from "../../services/usuariosService/usuario.service";
import {Anuncio} from "../../models/anuncio";
import {co} from "@fullcalendar/core/internal-common";

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
  anio: string;
  mes: string;
  clicks: number;
}
@Component({
  selector: 'vertical-bar-chart',
  templateUrl: './vertical-bar-chart.component.html',
  styleUrls: ['./vertical-bar-chart.component.css']
})


export class VerticalBarChartComponent implements OnInit, OnDestroy{
  @ViewChild("chartVert") chart: ChartComponent | undefined;
  private subscription:Subscription | undefined;
  public chartOptions: Partial<ChartOptions>;
  profesionistaId: number = 0;
  anunciosStats:Anuncio[] = [];
  mesAnios: MesAnio[] = [];

  constructor(private profesionistaService:ProfesionistasService,
              private userService:UsuarioService) {
    this.chartOptions = {
      series: [
        {
          name: "Clicks",
          data: []
        }
      ],
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
        title:{
          text:'Mes'
        }
      },
      yaxis:{
        title:{
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
        next:(response)=>{
          this.profesionistaId = response.id;
          this.cargarDatosAnuncios();
        }
      })
    );
  }

  private cargarDatosAnuncios() {
    this.subscription?.add(
      this.profesionistaService.getClicksAnuncio(this.profesionistaId).subscribe({
        next:(response)=>{
          this.anunciosStats = response;

          for (const a of this.anunciosStats) {
            let fechaa:Date = new Date(a.anio, a.mes-1);
            let nombreMes:string = fechaa.toLocaleDateString('es-ES', {month:"long"}).charAt(0).toUpperCase() + fechaa.toLocaleDateString('es-ES', {month:"long"}).slice(1);
            if (this.mesAnios.length != 0){
              for (const s of this.mesAnios) {
                if(!this.mesAnios.some(e => e.anio === fechaa.getFullYear().toString() && e.mes === nombreMes)){
                  this.mesAnios.push({anio:fechaa.getFullYear().toString(),mes:nombreMes, clicks:a.cantidadClicks})
                }
              }
            }else{
              this.mesAnios.push({anio:fechaa.getFullYear().toString(),mes:nombreMes, clicks:a.cantidadClicks})
            }
          }

          let a:string[] = this.mesAnios.map(m => m.mes +'/'+ m.anio.toString());
          if (this.chartOptions.xaxis) {
            this.chartOptions.xaxis.categories = a;
          }
          if (this.chartOptions.series){
            this.chartOptions.series[0].data = this.getDataForMonths();
          }
          this.chart?.updateOptions({
            xaxis:{
              categories: a,
              title:{
                text:'Mes'
              }
            }
          })
          setTimeout(()=> (window as any).dispatchEvent(new Event('resize')), 1)
        }
      })
    );
  }

  private getDataForMonths(): number[] {
    const data: number[] = [];
    const meses = this.mesAnios.map(m => m.mes);

    for (const mes of meses) {
      const mesAnio = this.mesAnios.find(m => m.mes === mes);
      data.push(mesAnio ? mesAnio.clicks : 0);
    }

    return data;
  }
}
