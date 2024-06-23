import {Component} from '@angular/core';
import {DashboardService} from "../dashboard.service";
import {UsuarioService} from "../../services/usuariosService/usuario.service";
import {Roles} from "../../models/Auxiliares/roles";
import {Router} from "@angular/router";

@Component({
  selector: 'top-bar',
  templateUrl: './top-bar.component.html',
  styleUrls: ['./top-bar.component.css']
})
export class TopBarComponent {
  constructor(private dashboard: DashboardService,
              private usuarioService:UsuarioService,
              private roter:Router) {
  }
  openSidebar(){
    this.dashboard.openSidebar()
  }
  esProfesionista():Boolean{
    return this.usuarioService.rolUsuario() == Roles.PROFESIONISTA;
  }
  esAdmin():Boolean{
    return this.usuarioService.rolUsuario() == Roles.ADMINISTRADOR;
  }

  cerrarSesion(){
    this.usuarioService.cerrarSesion();
    this.roter.navigate(['home'])
  }


  logueado():Boolean{
    return this.usuarioService.estaLogueado();
  }
}
