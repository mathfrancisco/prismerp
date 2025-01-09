import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DepartamentsDialogComponent } from './departaments-dialog.component';

describe('DepartamentsDialogComponent', () => {
  let component: DepartamentsDialogComponent;
  let fixture: ComponentFixture<DepartamentsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DepartamentsDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DepartamentsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
