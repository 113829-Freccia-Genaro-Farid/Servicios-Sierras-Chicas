import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import {
  ApexAxisChartSeries,
  ApexChart,
  ChartComponent,
  ApexDataLabels,
  ApexPlotOptions,
  ApexResponsive,
  ApexXAxis,
  ApexLegend,
  ApexFill, ApexTitleSubtitle
} from "ng-apexcharts";
import { UsuarioService } from "../../services/usuariosService/usuario.service";
import { Usuario } from "../../models/usuario";
import { Subscription } from "rxjs";

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  plotOptions: ApexPlotOptions;
  responsive: ApexResponsive[];
  xaxis: ApexXAxis;
  legend: ApexLegend;
  title: ApexTitleSubtitle;
  fill: ApexFill;
};

@Component({
  selector: 'grafico-usuarios-nuevos',
  templateUrl: './grafico-usuarios-nuevos.component.html',
  styleUrls: ['./grafico-usuarios-nuevos.component.css']
})
export class GraficoUsuariosNuevosComponent implements OnInit, OnDestroy {
  @ViewChild("grafico") grafico: ChartComponent | undefined;
  private subscription: Subscription | undefined;
  public chartOptions: Partial<ChartOptions>;
  usuarios: Usuario[] = [];
  availableYears: number[] = [];
  selectedYear: number | undefined;

  constructor(private usuarioService: UsuarioService) {
    this.chartOptions = {
      series: [],
      chart: {
        type: "bar",
        height: 350,
        stacked: true,
        toolbar: {
          show: true
        },
        zoom: {
          enabled: true
        }
      },
      responsive: [
        {
          breakpoint: 480,
          options: {
            legend: {
              position: "bottom",
              offsetX: -10,
              offsetY: 0
            }
          }
        }
      ],
      title: {
        text: "Clientes/Profesionistas nuevos por mes",
        align: "center"
      },
      plotOptions: {
        bar: {
          horizontal: false
        }
      },
      xaxis: {
        type: "category",
        categories: []
      },
      legend: {
        position: "right",
        offsetY: 40
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

    this.subscription.add(
      this.usuarioService.getUsuarios().subscribe({
        next: (response) => {
          this.usuarios = response;
          this.availableYears = Array.from(new Set(this.usuarios.map(usuario => new Date(usuario.fechaAlta).getFullYear()))).sort((a, b) => b - a);
          if (this.availableYears.length > 0) {
            this.selectedYear = this.availableYears[0];
            this.updateChart(this.selectedYear);
          }
        }
      })
    );
  }

  onYearChange(event: any): void {
    this.updateChart(event.value);
  }

  updateChart(year: number): void {
    const profCounts: { [key: string]: number } = {};
    const cliCounts: { [key: string]: number } = {};

    this.usuarios.forEach(usuario => {
      const fecha = new Date(usuario.fechaAlta);
      if (!isNaN(fecha.getTime()) && fecha.getFullYear() === year) {  // Validación de fecha y año
        const key = `${(fecha.getMonth() + 1).toString().padStart(2, '0')}/${fecha.getFullYear()}`;

        if (usuario.rol === 'PROFESIONISTA') {
          profCounts[key] = (profCounts[key] || 0) + 1;
        } else if (usuario.rol === 'CLIENTE') {
          cliCounts[key] = (cliCounts[key] || 0) + 1;
        }
      }
    });

    const categories = Array.from(new Set([...Object.keys(profCounts), ...Object.keys(cliCounts)])).sort((a, b) => {
      const [monthA, yearA] = a.split('/').map(Number);
      const [monthB, yearB] = b.split('/').map(Number);
      return new Date(yearA, monthA - 1).getTime() - new Date(yearB, monthB - 1).getTime();
    });

    const clienteData = categories.map(category => cliCounts[category] || 0);
    const profesionistasData = categories.map(category => profCounts[category] || 0);

    this.chartOptions.series = [
      {
        name: "CLIENTES",
        data: clienteData
      },
      {
        name: "PROFESIONISTAS",
        data: profesionistasData
      }
    ];
    this.grafico?.updateOptions({
      xaxis:{
        categories:categories
      }
    });
    setTimeout(()=> (window as any).dispatchEvent(new Event('resize')), 1)
  }
}
