import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {UsuarioService} from "../../../services/usuariosService/usuario.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Login} from "../../../models/login";
import {MensajeRespuesta} from "../../../models/mensaje-respuesta";
import {Router} from "@angular/router";

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
  alerta:boolean = false;
  constructor(private usuarioService:UsuarioService, private fb:FormBuilder, private router:Router) {}

  ngOnInit(): void {
    this.subscription = new Subscription();

    this.formLogin = this.fb.group({
      email: [null, [Validators.required]], // email validator
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

    this.subscription?.add(
      this.usuarioService.postLogin(this.login).subscribe({
        next: async(response:MensajeRespuesta) => {
          this.mensajeRespuesta = response;
          if (this.mensajeRespuesta.ok){
            this.formLogin.reset();
          }
          await this.toggleAlert();
        },
        error: async (response:MensajeRespuesta) => {
          this.mensajeRespuesta = response;
          await this.toggleAlert();
        }
      })
    )
  }

  async toggleAlert(): Promise<void> {
    this.alerta = !this.alerta;

    if (this.alerta) {
      await new Promise(resolve => setTimeout(resolve, 4000));
      this.alerta = false;
    }
  }

  get email(){
    return this.formLogin.get('email')
  }
  get password(){
    return this.formLogin.get('password')
  }


}
