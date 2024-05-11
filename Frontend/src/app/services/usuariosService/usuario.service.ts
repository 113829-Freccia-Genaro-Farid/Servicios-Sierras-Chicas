import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Login} from "../../models/login";
import {Observable} from "rxjs";
import {MensajeRespuesta} from "../../models/mensaje-respuesta";
import {Usuario} from "../../models/usuario";
import {UsuarioDTOPost} from "../../DTOs/usuario-dtopost";

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor(private client:HttpClient) { }
  postLogin(loginDTO:Login):Observable<MensajeRespuesta>{
    return this.client.post<MensajeRespuesta>("http://localhost:8080/api/login", loginDTO);
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
  cambioRolUsuario(email:string,idRol:string):Observable<MensajeRespuesta>{
    return this.client.put<MensajeRespuesta>("http://localhost:8080/api/usuarios/"+email, idRol);
  }
}
