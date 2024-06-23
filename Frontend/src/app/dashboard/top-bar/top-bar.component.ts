import {Component} from '@angular/core';
import {DashboardService} from "../dashboard.service";
import {UsuarioService} from "../../services/usuariosService/usuario.service";
import {Roles} from "../../models/Auxiliares/roles";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'top-bar',
  templateUrl: './top-bar.component.html',
  styleUrls: ['./top-bar.component.css']
})
export class TopBarComponent {
  constructor(private dashboard: DashboardService,
              private usuarioService:UsuarioService,
              private router:Router,
              private alerta: MatSnackBar) {
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
    this.alerta.open('Cerrando sesión...', '', {duration:2000});

    setTimeout(() => {
      this.usuarioService.cerrarSesion();
      this.router.navigate(['/home']);
    }, 2000);

  }


  logueado():Boolean{
    return this.usuarioService.estaLogueado();
  }
}
