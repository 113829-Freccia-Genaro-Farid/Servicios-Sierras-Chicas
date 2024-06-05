import {Component} from '@angular/core';
import {DashboardService} from "../dashboard.service";
import {UsuarioService} from "../../services/usuariosService/usuario.service";
import {Roles} from "../../models/Auxiliares/roles";
import {Router} from "@angular/router";

@Component({
  selector: 'top-bar',
  templateUrl: './top-bar.component.html'
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
    console.log(this.usuarioService.rolUsuario())
    console.log(Roles.PROFESIONISTA)
    return this.usuarioService.rolUsuario() == Roles.PROFESIONISTA;
  }

  cerrarSesion(){
    this.usuarioService.cerrarSesion();
    this.roter.navigate(['home'])
  }

  logueado():Boolean{
    return this.usuarioService.estaLogueado();
  }
}
