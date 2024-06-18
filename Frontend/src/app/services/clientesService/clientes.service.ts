import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Profesionista} from "../../models/profesionista";
import {Cliente} from "../../models/cliente";

@Injectable({
  providedIn: 'root'
})
export class ClientesService {

  constructor(private client:HttpClient) { }
  getClienteById(idCliente:number): Observable<Cliente> {
    return this.client.get<Profesionista>("http://localhost:8080/api/clientes/"+idCliente);
  }
  getClientes(): Observable<Cliente[]> {
    return this.client.get<Profesionista[]>("http://localhost:8080/api/clientes");
  }
  getClienteByUserEmail(userEmail:string): Observable<Cliente> {
    return this.client.get<Profesionista>("http://localhost:8080/api/clientes/byUserEmail/"+userEmail);
  }
}
