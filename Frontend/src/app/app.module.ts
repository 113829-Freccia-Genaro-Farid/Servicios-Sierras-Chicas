import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {RouterModule, Routes} from "@angular/router";

import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login/login.component';
import { RegistrarUsuarioComponent } from './components/usuario/registrar-usuario/registrar-usuario.component';
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import {AlertComponent} from "./components/ventanas/alert/alert.component";
import {ModalComponent} from "./components/ventanas/modal/modal.component";

const routes:Routes = [
  {path: '', component:RegistrarUsuarioComponent},
  {path: 'home', component:HomeComponent},
  {path: 'login', component:LoginComponent}
]
@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    RegistrarUsuarioComponent,
    AlertComponent,
    ModalComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(routes),
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
