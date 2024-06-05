import {Component, OnInit} from '@angular/core';
import {AuxiliaresService} from "../../services/auxiliaresService/auxiliares.service";
declare const MercadoPago: any; // Declara MercadoPago para que TypeScript lo reconozca

@Component({
  selector: 'mercado-pago',
  templateUrl: './mercado-pago.component.html',
  styleUrls: ['./mercado-pago.component.css']
})
export class MercadoPagoComponent implements OnInit {

  constructor(
    private auxiliaresService: AuxiliaresService
  ) { }

  ngOnInit(): void {
    this.initMercadoPago()
  }

  initMercadoPago() {
    const mp = new MercadoPago('APP_USR-6531b60e-c4b6-4ad1-a8c0-7b9dcfb45b56', {
      locale: 'es-AR'
    });

    const anuncionsPremium = {
      title: 'Anuncios Premium',
      quantity: 1,
      price: 2500
    };

    this.auxiliaresService.createPreference(anuncionsPremium).subscribe(preference => {
      this.createCheckoutButton(mp, preference);
    }, error => {
      alert("error: " + error);
    });
  }

  createCheckoutButton(mp: any, preferenceId: string) {
    const bricksBuilder = mp.bricks();
    const generateButton = async () => {
      if ((window as any).checkoutButton) (window as any).checkoutButton.unmount();
      (window as any).checkoutButton = await bricksBuilder.create('wallet', 'wallet_container', {
        initialization: {
          preferenceId: preferenceId,
        }
      });
    }
    generateButton();
  }
}
