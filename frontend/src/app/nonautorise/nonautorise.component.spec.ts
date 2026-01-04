import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NonautoriseComponent } from './nonautorise.component';

describe('NonautoriseComponent', () => {
  let component: NonautoriseComponent;
  let fixture: ComponentFixture<NonautoriseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NonautoriseComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NonautoriseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
