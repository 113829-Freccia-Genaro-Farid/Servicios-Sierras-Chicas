import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MensajeRespuesta} from "../../../models/mensaje-respuesta";
import {Login} from "../../../models/login";
import {UsuarioService} from "../../../services/usuariosService/usuario.service";
import {Router} from "@angular/router";
import {UsuarioDTOPost} from "../../../DTOs/usuario-dtopost";
import {CustomValidators} from "../../../customValidators/custom-validators";

@Component({
  selector: 'app-registrar-usuario',
  templateUrl: './registrar-usuario.component.html',
  styleUrls: ['./registrar-usuario.component.css']
})
export class RegistrarUsuarioComponent implements OnInit,OnDestroy{

  private subscription:Subscription | undefined;
  formUsuario:FormGroup = this.fb.group({});
  mensajeRespuesta:MensajeRespuesta = {} as MensajeRespuesta;
  usuario:UsuarioDTOPost = {} as UsuarioDTOPost;
  alerta:boolean = false;
  constructor(private usuarioService:UsuarioService, private fb:FormBuilder, private router:Router) {}

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  ngOnInit(): void {
    this.subscription = new Subscription();

    this.formUsuario = this.fb.group({
      email: [null, [Validators.required, Validators.email]], // email validator
      password: [null, [Validators.required, Validators.minLength(8)]],
      confirmpassword: [null, [Validators.required, CustomValidators.mismaPassword]]
    });
  }

  registrarUsuario(){
    this.usuario = {
      email:this.formUsuario.get("email")?.value,
      password:this.formUsuario.get("password")?.value
    } as Login;

    this.subscription?.add(
      this.usuarioService.postUsuario(this.usuario).subscribe({
        next: async(response:MensajeRespuesta) => {
          this.mensajeRespuesta = response;
          if (this.mensajeRespuesta.ok){
            this.formUsuario.reset();
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
    return this.formUsuario.get('email')
  }
  get password(){
    return this.formUsuario.get('password')
  }
  get confirmpassword(){
    return this.formUsuario.get('confirmpassword')
  }

}
