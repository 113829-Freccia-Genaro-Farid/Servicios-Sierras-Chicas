import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Login} from "../../models/login";
import {Observable, tap} from "rxjs";
import {MensajeRespuesta} from "../../models/mensaje-respuesta";
import {Usuario} from "../../models/usuario";
import {UsuarioDTOPost} from "../../DTOs/usuario-dtopost";
import {Roles} from "../../models/Auxiliares/roles";

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  constructor(private client: HttpClient) { }

  estaLogueado():Boolean{
    return this.getUsuarioLogueado().email != null;
  }
  getUsuarioLogueado(): Usuario {
    return JSON.parse(localStorage.getItem('user') || '{}') as Usuario;
  }
  cerrarSesion(){
    localStorage.removeItem('user');
  }
  rolUsuario():Roles{
    return this.getUsuarioLogueado().rol;
  }

  postLogin(loginDTO: Login): Observable<MensajeRespuesta> {
    return this.client.post<MensajeRespuesta>("http://localhost:8080/api/login", loginDTO)
      .pipe(
        tap((respuesta: MensajeRespuesta) => {
          if (respuesta.ok) {
            this.getUsuarioByEmail(loginDTO.email).subscribe((response: Usuario) => {
              localStorage.setItem('user', JSON.stringify(response));
            });
          }
        })
      );
  }
  getUsuarios(): Observable<Usuario[]> {
    return this.client.get<Usuario[]>("http://localhost:8080/api/usuarios");
  }
  getUsuarioByEmail(email:string): Observable<Usuario> {
    return this.client.get<Usuario>("http://localhost:8080/api/usuarios/"+email);
  }
  getUsuariosFilterByEmail(email:string): Observable<Usuario[]> {
    return this.client.get<Usuario[]>("http://localhost:8080/api/usuarios/filtro?email="+email);
  }
  getUsuariosByRol(idRol:BigInt): Observable<Usuario[]> {
    return this.client.get<Usuario[]>("http://localhost:8080/api/usuarios/rol?idRol="+idRol);
  }
  postUsuario(usuarioDTO:UsuarioDTOPost):Observable<MensajeRespuesta>{
    return this.client.post<MensajeRespuesta>("http://localhost:8080/api/usuarios", usuarioDTO);
  }
  bajaUsuario(email:string):Observable<MensajeRespuesta>{
    return this.client.delete<MensajeRespuesta>("http://localhost:8080/api/usuarios/"+email);
  }
  eliminarUsuarioSistema(email:string):Observable<MensajeRespuesta>{
    let e = encodeURIComponent(email)
    return this.client.post<MensajeRespuesta>("http://localhost:8080/api/usuarios/deleteUser/"+e,{});
  }
  cambioRolUsuario(email:string,idRol:string):Observable<MensajeRespuesta>{
    return this.client.put<MensajeRespuesta>("http://localhost:8080/api/usuarios/"+email, idRol);
  }
  verificarCodigo(email: string, codigo: string): Observable<MensajeRespuesta> {
    const body = {
      email: email,
      codigo: codigo
    };
    return this.client.post<MensajeRespuesta>("http://localhost:8080/api/usuarios/verificarCodigo", body);
  }
  enviarCorreo(email:string):Observable<MensajeRespuesta>{
    const body = {
      mailTo: email
    };
    return this.client.post<MensajeRespuesta>("http://localhost:8080/email/send-html", body);
  }
  cambiarContrasenia(email: string, pass: string):Observable<MensajeRespuesta>{
    const body = {
      email: email,
      password: pass
    };
    return this.client.post<MensajeRespuesta>("http://localhost:8080/api/usuarios/cambioPassword", body);
  }
}
