import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MercadoPagoComponent } from './mercado-pago.component';

describe('MercadoPagoComponent', () => {
  let component: MercadoPagoComponent;
  let fixture: ComponentFixture<MercadoPagoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MercadoPagoComponent]
    });
    fixture = TestBed.createComponent(MercadoPagoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
