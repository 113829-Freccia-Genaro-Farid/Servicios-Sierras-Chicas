import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {MatDialog} from "@angular/material/dialog";
import {DialogGenericoComponent} from "../ventanas/dialog-generico/dialog-generico.component";
import {UsuarioService} from "../../services/usuariosService/usuario.service";
import {Router} from "@angular/router";
import {Roles} from "../../models/Auxiliares/roles";

@Component({
  selector: 'preguntas-frecuentes',
  templateUrl: './preguntas-frecuentes.component.html',
  styleUrls: ['./preguntas-frecuentes.component.css']
})
export class PreguntasFrecuentesComponent implements OnInit, OnDestroy{
  private subscription:Subscription | undefined;
  constructor(private dialog: MatDialog,
              private usuarioService:UsuarioService,
              private router:Router) {
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  ngOnInit(): void {
    this.subscription = new Subscription();
  }

  openDialog(): void {
    if(!this.usuarioService.estaLogueado()){
      this.router.navigate(['/login'])
      return;
    }

    const dialogRef1 = this.dialog.open(DialogGenericoComponent, {
      data: {
        title: 'Baja del sistema',
        sinBotones: false,
        descripcion: 'Por favor ten en cuenta que eliminar todos tus datos del sistema es un proceso irreversible. ' +
          '¿Estás completamente seguro de que deseas continuar?'
      }
    });

    dialogRef1.afterClosed().subscribe(result1 => {
      if (result1) {
        const dialogRef2 = this.dialog.open(DialogGenericoComponent, {
          data: {
            title: 'Baja del sistema',
            sinBotones: false,
            descripcion: '¿Estás completamente seguro de darte de baja?'
          }
        });

        dialogRef2.afterClosed().subscribe(result2 => {
          if (result2) {
            this.borrarUsuario();
            this.router.navigate(['/home']);
          }
        });
      }
    });
  }


  borrarUsuario() {
    this.subscription?.add(
      this.usuarioService.eliminarUsuarioSistema(this.usuarioService.getUsuarioLogueado().email).subscribe({
        next:(response)=>{
          console.log(response)
          this.usuarioService.cerrarSesion();
        },
        error:(response)=>{
          console.log(response)
        }
      })
    )
    this.usuarioService.cerrarSesion();
  }

  comprobarLogin() {
    if(!this.usuarioService.estaLogueado()){
      this.router.navigate(['/login'])
    }
  }

  esProf():boolean{
    if(this.usuarioService.estaLogueado()){
      if(this.usuarioService.rolUsuario() == Roles.PROFESIONISTA){
        return true;
      }
      else{
        return false;
      }
    }else{
      return false;
    }
  }

  protected readonly Roles = Roles;
}
