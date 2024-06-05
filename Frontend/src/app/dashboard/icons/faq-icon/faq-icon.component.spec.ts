import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FaqIconComponent } from './faq-icon.component';

describe('FaqIconComponent', () => {
  let component: FaqIconComponent;
  let fixture: ComponentFixture<FaqIconComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FaqIconComponent]
    });
    fixture = TestBed.createComponent(FaqIconComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
