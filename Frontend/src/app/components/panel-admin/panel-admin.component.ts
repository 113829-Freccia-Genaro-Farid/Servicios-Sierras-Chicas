import {Component, OnDestroy, OnInit} from '@angular/core';
import {UsuarioService} from "../../services/usuariosService/usuario.service";
import {Roles} from "../../models/Auxiliares/roles";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {AgregarTurnoComponent} from "../turnos/agregar-turno/agregar-turno/agregar-turno.component";
import {Turno} from "../../models/turno";
import {ListadoUsuariosComponent} from "./listados/listado-usuarios/listado-usuarios.component";
import {ListadoProfesionistasComponent} from "./listados/listado-profesionistas/listado-profesionistas.component";
import {ListadoClientesComponent} from "./listados/listado-clientes/listado-clientes.component";

@Component({
  selector: 'panel-admin',
  templateUrl: './panel-admin.component.html',
  styleUrls: ['./panel-admin.component.css']
})
export class PanelAdminComponent implements OnInit,OnDestroy{
  constructor(private userService:UsuarioService,
              private router:Router,
              private dialog: MatDialog) {
  }
  ngOnDestroy(): void {
  }

  ngOnInit(): void {
    if(this.userService.rolUsuario() != Roles.ADMINISTRADOR){
      this.router.navigate(['home'])
    }
  }

  abrirListadoUsuarios() {
    this.dialog.open(ListadoUsuariosComponent, {
    });
  }

  abrirListadoProfesionistas() {
    this.dialog.open(ListadoProfesionistasComponent, {
    });
  }
  abrirListadoClientes() {
    this.dialog.open(ListadoClientesComponent, {
    });
  }
}
