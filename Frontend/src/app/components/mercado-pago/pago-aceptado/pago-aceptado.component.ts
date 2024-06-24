import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProfesionistasService} from "../../../services/profesionistasService/profesionistas.service";
import {UsuarioService} from "../../../services/usuariosService/usuario.service";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Roles} from "../../../models/Auxiliares/roles";

@Component({
  selector: 'pago-aceptado',
  templateUrl: './pago-aceptado.component.html',
  styleUrls: ['./pago-aceptado.component.css']
})
export class PagoAceptadoComponent implements OnInit, OnDestroy{
  private sub:Subscription | undefined;
  constructor(private profService:ProfesionistasService,
              private userService:UsuarioService,
              private alerta: MatSnackBar,
              private router:Router) {
  }
  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  ngOnInit(): void {
    this.sub = new Subscription();

    if (this.userService.estaLogueado() && this.userService.rolUsuario() == Roles.PROFESIONISTA){
      this.sub?.add(
        this.profService.getProfesionistaByUserEmail(this.userService.getUsuarioLogueado().email).subscribe({
          next:(response)=>{
            this.profService.putSuscripcionProfesionista(response.id).subscribe({
              next:(response)=>{
                this.alerta.open(response.mensaje,"Cerrar",{duration:3000});
                this.router.navigate(['/home']);
              },
              error:(response)=>{
                this.router.navigate(['/home']);
              }
            })
          }
        })
      )
    }else{
      this.router.navigate(['/home']);
    }
  }

}
