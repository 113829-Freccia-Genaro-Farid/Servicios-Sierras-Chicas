import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {UsuarioService} from "../../../../services/usuariosService/usuario.service";
import {Usuario} from "../../../../models/usuario";

@Component({
  selector: 'listado-usuarios',
  templateUrl: './listado-usuarios.component.html',
  styleUrls: ['./listado-usuarios.component.css']
})
export class ListadoUsuariosComponent implements OnInit, OnDestroy{
  private sub:Subscription | undefined;
  usuarios:Usuario[] = [];

  constructor(private userService:UsuarioService) {
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  ngOnInit(): void {
    this.sub = new Subscription();

    this.sub.add(
      this.userService.getUsuarios().subscribe({
        next:(response) =>{
          this.usuarios = response;
        }
      })
    )
  }
  consultarUsuario(usuario: Usuario) {
    console.log('Consultar usuario:', usuario);
  }

  editarUsuario(usuario: Usuario) {
    console.log('Editar usuario:', usuario);
  }

  eliminarUsuario(usuario: Usuario) {
    console.log('Eliminar usuario:', usuario);
  }
}
