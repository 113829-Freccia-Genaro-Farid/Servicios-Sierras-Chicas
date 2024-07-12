import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {UsuarioService} from "../../../services/usuariosService/usuario.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Login} from "../../../models/login";
import {MensajeRespuesta} from "../../../models/mensaje-respuesta";
import {Router} from "@angular/router";
import {DialogGenericoComponent} from "../../ventanas/dialog-generico/dialog-generico.component";
import {RecuperarContrasenaComponent} from "../recuperar-contrasena/recuperar-contrasena.component";
import {MatDialog} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy{

  private subscription:Subscription | undefined;
  formLogin:FormGroup = this.fb.group({});
  mensajeRespuesta:MensajeRespuesta = {} as MensajeRespuesta;
  login:Login = {} as Login;
  hidePassword = true;
  constructor(private usuarioService:UsuarioService,
              private fb:FormBuilder,
              private router:Router,
              private dialog: MatDialog,
              private alerta: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.subscription = new Subscription();

    this.formLogin = this.fb.group({
      email: [null, [Validators.required, Validators.email]], // email validator
      password: [null, [Validators.required]],
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  enviarLogin(){
    this.login = {
      email:this.formLogin.get("email")?.value,
      password:this.formLogin.get("password")?.value
    } as Login;

    this.alerta.open('Iniciando sesión...', '', {duration:2000});
    setTimeout(() => {
      this.subscription?.add(
        this.usuarioService.postLogin(this.login).subscribe({
          next: async(response:MensajeRespuesta) => {
            this.mensajeRespuesta = response;
            this.openSnackBar(this.mensajeRespuesta.mensaje);
            if (this.mensajeRespuesta.ok){
              this.formLogin.reset();
              await this.router.navigate(['home'])
            }
          },
          error: async (response:MensajeRespuesta) => {
            this.mensajeRespuesta = response;
            this.openSnackBar(this.mensajeRespuesta.mensaje);
          }
        })
      )
    }, 2000);


  }

  get email(){
    return this.formLogin.get('email')
  }
  get password(){
    return this.formLogin.get('password')
  }
  openDialog(): void {
    this.dialog.open(DialogGenericoComponent, {
      data: {
        title: 'Recuperar contraseña',
        component: RecuperarContrasenaComponent,
        sinBotones: true
      }
    });
  }
  openSnackBar(mensaje:string) {
    this.alerta.open(mensaje,"Cerrar",{duration:3000});
  }
}
