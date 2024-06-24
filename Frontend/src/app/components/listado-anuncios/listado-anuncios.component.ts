import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ProfesionistasService} from "../../services/profesionistasService/profesionistas.service";
import {Subscription} from "rxjs";
import {Profesionista} from "../../models/profesionista";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {Categoria} from "../../models/Auxiliares/categoria";
import {AuxiliaresService} from "../../services/auxiliaresService/auxiliares.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Profesion} from "../../models/Auxiliares/profesion";
import {Ciudad} from "../../models/Auxiliares/ciudad";
import {MensajeRespuesta} from "../../models/mensaje-respuesta";
import {Router} from "@angular/router";

@Component({
  selector: 'listado-anuncios',
  templateUrl: './listado-anuncios.component.html',
  styleUrls: ['./listado-anuncios.component.css']
})
export class ListadoAnunciosComponent implements OnInit,OnDestroy{
  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;
  private subscription:Subscription | undefined;
  filtrosForm: FormGroup = this.fb.group({});
  profesionistas: Profesionista[] = [];
  categorias: Categoria[] = [];
  profesiones: Profesion[] = [];
  ciudades: Ciudad[] = [];
  pagedItems: Profesionista[] = [];
  pageSize = 5;
  pageIndex = 0;

  constructor(private profesionistasService: ProfesionistasService,
              private auxiliaresService: AuxiliaresService,
              private fb: FormBuilder,
              private router:Router) {}

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }
  ngOnInit(): void {
    this.subscription = new Subscription();
    this.filtrosForm = this.fb.group({
      nombre:[''],
      categorias: [[]],
      profesiones: [[]],
      ciudades: [[]]
    });
    this.filtrosForm.controls['categorias'].valueChanges.subscribe((value) => {
      if (this.categoriasControl?.value.length == 1){
        this.cargarProfesiones(value);
      }else{
        this.profesiones = [];
      }
    })

    this.subscription.add(
      this.profesionistasService.getProfesionistasOrdSuscrito().subscribe({
        next: (response) => {
          this.profesionistas = response;
          this.updatePagedItems();
          this.obtenerPromedios();
        },
        error: () => {
          alert('Error al cargar los profesionistas');
        }
      })
    )

    this.subscription.add(
      this.auxiliaresService.getCategorias().subscribe({
        next: (response) => {
          this.categorias = response;
        },
        error: () => {
          alert('Error al cargar las categorias');
        }
      })
    )

    this.subscription.add(
      this.auxiliaresService.getCiudades().subscribe({
        next: (response) => {
          this.ciudades = response;
        },
        error: () => {
          alert('Error al cargar las ciudades');
        }
      })
    )
  }
  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.updatePagedItems();
  }

  updatePagedItems() {
    const startIndex = this.pageIndex * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.pagedItems = this.profesionistas.slice(startIndex, endIndex);
  }

  cargarProfesiones(value:number){
    this.subscription?.add(
      this.auxiliaresService.getProfesionesByCategoria(value).subscribe({
        next:(response)=>{
          this.profesiones = response;
        }
      })
    )
  }
  get ciudadesControl(){
    return this.filtrosForm.get('ciudades');
  }
  get categoriasControl(){
    return this.filtrosForm.get('categorias');
  }
  get profesionesControl(){
    return this.filtrosForm.get('profesiones');
  }
  get nombre(){
    return this.filtrosForm.get('nombre');
  }

  filtrarProfesionistas() {
    this.pageIndex = 0;
    if (this.paginator) {
      this.paginator.firstPage();
    }
    this.subscription?.add(
      this.profesionistasService.getProfesionistasFiltros(this.nombre?.value, this.categoriasControl?.value, this.profesionesControl?.value, this.ciudadesControl?.value).subscribe({
        next:(response)=>{
          this.profesionistas = response;
          this.updatePagedItems();
        },
        error:() => {
          alert('Error al filtrar los profesionistas');
        }
      })
    )
  }

  limparFiltros() {
    this.filtrosForm.reset();
  }

  clickAnuncio(idProfesionista: number): void {
    this.profesionistasService.postClickAnuncio(idProfesionista).subscribe({
      next: (respuesta: MensajeRespuesta) => {
      },
      error: (error) => {
      }
    });
    this.router.navigate(['/profesionistas',idProfesionista]);
  }

  obtenerPromedios(){
    for (const p of this.profesionistas) {
      this.subscription?.add(
        this.profesionistasService.getPromedioReseniasByProfesionista(p.id).subscribe({
          next:(response) =>{
            p.promedioResenias = response;
          }
        })
      )
    }
  }
}
