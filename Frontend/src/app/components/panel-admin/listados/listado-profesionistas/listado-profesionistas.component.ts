import {Component, OnDestroy, OnInit} from '@angular/core';
import {Profesionista} from "../../../../models/profesionista";
import {Subscription} from "rxjs";
import {ProfesionistasService} from "../../../../services/profesionistasService/profesionistas.service";

@Component({
  selector: 'listado-profesionistas',
  templateUrl: './listado-profesionistas.component.html',
  styleUrls: ['./listado-profesionistas.component.css']
})
export class ListadoProfesionistasComponent implements OnInit, OnDestroy{
  private sub:Subscription | undefined;
  profesionistas:Profesionista[] = [];

  constructor(private profService:ProfesionistasService) {
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  ngOnInit(): void {
    this.sub = new Subscription();

    this.sub.add(
      this.profService.getProfesionistas().subscribe({
        next:(response) =>{
          this.profesionistas = response;
        }
      })
    )
  }
  consultarProfesionista(profesionista:Profesionista) {

  }

  editarProfesionista(profesionista:Profesionista) {

  }

  eliminarProfesionista(profesionista:Profesionista) {

  }
}
