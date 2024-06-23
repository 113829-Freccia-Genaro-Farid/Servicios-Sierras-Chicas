import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EstrellasReseniaComponent } from './estrellas-resenia.component';

describe('EstrellasReseniaComponent', () => {
  let component: EstrellasReseniaComponent;
  let fixture: ComponentFixture<EstrellasReseniaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EstrellasReseniaComponent]
    });
    fixture = TestBed.createComponent(EstrellasReseniaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
