import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {RouterModule, Routes} from "@angular/router";

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login/login.component';
import { RegistrarUsuarioComponent } from './components/usuario/registrar-usuario/registrar-usuario.component';
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import {AlertComponent} from "./components/ventanas/alert/alert.component";
import {ModalComponent} from "./components/ventanas/modal/modal.component";
import { ModificarPersonaComponent } from './components/personas/modificar-persona/modificar-persona.component';
import { MercadoPagoComponent } from './components/mercado-pago/mercado-pago.component';
import { HomeComponent } from './components/home/home.component';
import {LayoutComponent} from "./dashboard/layout/layout.component";
import {TopBarComponent} from "./dashboard/top-bar/top-bar.component";
import {OverlayComponent} from "./dashboard/overlay/overlay.component";
import {DocIconComponent} from "./dashboard/icons/doc-icon/doc-icon.component";
import {AppIconComponent} from "./dashboard/icons/app-icon/app-icon.component";
import {BillIconComponent} from "./dashboard/icons/bill-icon/bill-icon.component";
import {HomeIconComponent} from "./dashboard/icons/home-icon/home-icon.component";
import {AnalyticIconComponent} from "./dashboard/icons/analytic-icon/analytic-icon.component";
import {MonitoringIconComponent} from "./dashboard/icons/monitoring-icon/monitoring-icon.component";
import {DemographicIconComponent} from "./dashboard/icons/demographic-icon/demographic-icon.component";
import { ServiceIconComponent } from './dashboard/icons/service-icon/service-icon.component';
import { FaqIconComponent } from './dashboard/icons/faq-icon/faq-icon.component';
import {SidebarComponent} from "./dashboard/sidebar/sidebar/sidebar.component";
import {SidebarItemComponent} from "./dashboard/sidebar/sidebar-item/sidebar-item.component";
import {SidebarItemsComponent} from "./dashboard/sidebar/sidebar-items/sidebar-items.component";
import {SidebarHeaderComponent} from "./dashboard/sidebar/sidebar-header/sidebar-header.component";
import { TermsIconComponent } from './dashboard/icons/terms-icon/terms-icon.component';
import { TermsConditionsComponent } from './components/terms-conditions/terms-conditions.component';
import { PreguntasFrecuentesComponent } from './components/preguntas-frecuentes/preguntas-frecuentes.component';
import { UserIconComponent } from './dashboard/icons/user-icon/user-icon.component';
import { SearchIconComponent } from './dashboard/icons/search-icon/search-icon.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatMenuModule} from "@angular/material/menu";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";

const routes:Routes = [
  {path: 'home', component:HomeComponent},
  {path: 'register', component:RegistrarUsuarioComponent},
  {path: 'login', component:LoginComponent},
  {path: 'datospersonales', component:ModificarPersonaComponent},
  {path: 'pagosuscripcion', component:MercadoPagoComponent},
  {path: 'terms', component:TermsConditionsComponent},
  {path: 'faq', component:PreguntasFrecuentesComponent},

  {path: '', redirectTo: 'home', pathMatch: 'full' },
  {path: '**', redirectTo: 'home' },
]
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrarUsuarioComponent,
    AlertComponent,
    ModalComponent,
    ModificarPersonaComponent,
    MercadoPagoComponent,
    HomeComponent,
    TermsConditionsComponent,
    PreguntasFrecuentesComponent,

    // dashboard
    LayoutComponent,
    TopBarComponent,
    OverlayComponent,
    SidebarComponent,
    SidebarItemComponent,
    SidebarItemsComponent,
    SidebarHeaderComponent,

    // icons
    DocIconComponent,
    AppIconComponent,
    BillIconComponent,
    HomeIconComponent,
    AnalyticIconComponent,
    MonitoringIconComponent,
    DemographicIconComponent,
    ServiceIconComponent,
    FaqIconComponent,
    TermsIconComponent,
    UserIconComponent,
    SearchIconComponent,

  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(routes),
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
