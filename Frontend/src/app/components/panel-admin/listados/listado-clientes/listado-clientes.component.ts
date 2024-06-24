import {Component, OnDestroy, OnInit} from '@angular/core';
import {Cliente} from "../../../../models/cliente";
import {Subscription} from "rxjs";
import {Profesionista} from "../../../../models/profesionista";
import {ProfesionistasService} from "../../../../services/profesionistasService/profesionistas.service";
import {ClientesService} from "../../../../services/clientesService/clientes.service";

@Component({
  selector: 'listado-clientes',
  templateUrl: './listado-clientes.component.html',
  styleUrls: ['./listado-clientes.component.css']
})
export class ListadoClientesComponent implements OnInit, OnDestroy{
  private sub:Subscription | undefined;
  clientes:Cliente[] = [];

  constructor(private clienteService:ClientesService) {
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  ngOnInit(): void {
    this.sub = new Subscription();

    this.sub.add(
      this.clienteService.getClientes().subscribe({
        next:(response) =>{
          this.clientes = response;
        }
      })
    )
  }
  consultarCliente(cliente:Cliente) {

  }

  editarCliente(cliente:Cliente) {

  }

  eliminarCliente(cliente:Cliente) {

  }
}
