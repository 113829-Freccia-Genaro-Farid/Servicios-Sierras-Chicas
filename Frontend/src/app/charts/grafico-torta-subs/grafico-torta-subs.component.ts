import {Component, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {
  ApexNonAxisChartSeries,
  ApexResponsive,
  ApexChart, ChartComponent
} from "ng-apexcharts";
import {Subscription} from "rxjs";
import {ProfesionistasService} from "../../services/profesionistasService/profesionistas.service";
import {Profesionista} from "../../models/profesionista";
import {Cliente} from "../../models/cliente";
import {ClientesService} from "../../services/clientesService/clientes.service";

export type ChartOptions = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  responsive: ApexResponsive[];
  labels: any;
};
@Component({
  selector: 'grafico-torta-subs',
  templateUrl: './grafico-torta-subs.component.html',
  styleUrls: ['./grafico-torta-subs.component.css']
})
export class GraficoTortaSubsComponent implements OnInit, OnDestroy{
  @ViewChild("grafico") chart: ChartComponent | undefined;
  @Input() tipo:number = 0;
  public chartOptions: Partial<ChartOptions>;
  private sub:Subscription | undefined;
  profesionistas:Profesionista[] = [];
  clientes: Cliente[] = [];

  constructor(private profService:ProfesionistasService,
              private clienteService:ClientesService) {
    this.chartOptions = {
      series: [],
      chart: {
        width: 380,
        type: "pie"
      },
      labels: ["Suscritos", "No Suscritos"],
      responsive: [
        {
          breakpoint: 480,
          options: {
            chart: {
              width: 200
            },
            legend: {
              position: "bottom"
            }
          }
        }
      ]
    };
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  ngOnInit(): void {
    this.sub = new Subscription();
    if(this.tipo == 0){
      this.sub.add(
        this.profService.getProfesionistas().subscribe({
          next:(response) => {
            let sub= 0;
            let noSub = 0;
            this.profesionistas = response;
            this.profesionistas.forEach(p=>{
              if(p.suscrito){
                sub++;
              }else{
                noSub++;
              }
            })
            this.chart?.updateOptions({
              series:[sub,noSub],
              labels: ["Suscritos", "No Suscritos"],
            })
          }
        })
      )
    }
    if (this.tipo==1){
      this.sub.add(
        this.profService.getProfesionistas().subscribe({
          next:(response) => {
            this.profesionistas = response;
            this.clienteService.getClientes().subscribe({
              next:(response) => {
                this.clientes = response;
                this.chart?.updateOptions({
                  series:[this.profesionistas.length, this.clientes.length],
                  labels: ["Profesionistas", "Clientes"],
                });
                setTimeout(()=> (window as any).dispatchEvent(new Event('resize')), 1)
              }
            })
          }
        })
      );
    }
  }
}
