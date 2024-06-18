import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UsuarioService} from "../../../services/usuariosService/usuario.service";
import {CustomValidators} from "../../../customValidators/custom-validators";
import {Subscription} from "rxjs";
import {MensajeRespuesta} from "../../../models/mensaje-respuesta";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'cambiar-contrasenia',
  templateUrl: './cambiar-contrasenia.component.html',
  styleUrls: ['./cambiar-contrasenia.component.css']
})
export class CambiarContraseniaComponent implements OnInit {
  subscription:Subscription = new Subscription();
  mensaje:MensajeRespuesta = {} as MensajeRespuesta;
  solicitaCodigo: boolean = false;
  email:string = '';
  hide:boolean = true;
  hide2:boolean = true;
  form:FormGroup = this.fb.group({});

  constructor(private fb: FormBuilder,
              private usuarioService: UsuarioService,
              private alerta: MatSnackBar,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private dialogRef: MatDialogRef<CambiarContraseniaComponent>
              ) {
  }

  ngOnInit() {
    this.solicitaCodigo = this.data.solicitaCodigo;
    this.email = this.data.email;
    this.form = this.fb.group({
      codigo: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmacionContrasena: ['', [Validators.required, CustomValidators.mismaPassword, Validators.minLength(8)]]
    });
    this.form.get('password')?.valueChanges.subscribe(() => {
      this.form.get('confirmacionContrasena')?.updateValueAndValidity();
    });
  }


  clickEvent(event: MouseEvent) {
    this.hide = !this.hide;
    event.stopPropagation();
  }
  clickEvent2(event: MouseEvent) {
    this.hide2 = !this.hide2;
    event.stopPropagation();
  }

  async restablecerContrasena() {
    const codigo = this.form.get('codigo')?.value;
    const nuevaContrasena = this.form.get('password')?.value;

    this.subscription?.add(
      this.usuarioService.verificarCodigo(this.email,codigo).subscribe({
        next: async(response)=>{
          if (response.ok){
            this.subscription.add(
              this.usuarioService.cambiarContrasenia(this.email, nuevaContrasena).subscribe({
                next:async(response)=>{
                  this.mensaje = response;
                  this.openSnackBar(this.mensaje.mensaje);
                  if (this.mensaje.ok) {
                    setTimeout(() => {
                      this.dialogRef.close();
                    }, 3000);
                  }
                },
                error:async()=>{
                  this.mensaje = { mensaje: "Error al cambiar de contraseña", ok: false };
                  this.openSnackBar(this.mensaje.mensaje);
                }
              })
            );
          }else{
            this.openSnackBar(response.mensaje);
          }
        },
        error:async()=>{
          this.mensaje = { mensaje: "Error al verificar codigo", ok: false };
          this.openSnackBar(this.mensaje.mensaje);
        }
      })
    );
  }

  openSnackBar(mensaje:string) {
    this.alerta.open(mensaje,"Cerrar",{duration:3000});
  }
  get codigo(){
    return this.form.get('codigo');
  }
  get pass1(){
    return this.form.get('password');
  }
  get pass2(){
    return this.form.get('confirmacionContrasena');
  }
}
