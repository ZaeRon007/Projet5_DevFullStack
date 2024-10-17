import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { of } from 'rxjs';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockSessionService: any;
  let mockUserService: any;
  let mockRouterService: any;
  let mockMatSnackBar: any;
  
  beforeEach(async () => {

    mockUserService = {
      delete: jest.fn().mockReturnValue(of({}))
    };

    mockSessionService = {
      sessionInformation: {id: 1},
      logOut: jest.fn()
    };

    mockRouterService = {
      navigate: jest.fn()
    };

    mockMatSnackBar = {
      open: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: UserService, useValue: mockUserService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouterService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
  });

  it('should delete user, show a message, log out and navigate to home page', () => {
    component.delete();

    expect(mockUserService.delete).toHaveBeenCalledWith('1');

    expect(mockMatSnackBar.open).toHaveBeenCalledWith(
      'Your account has been deleted !', 'Close', { duration: 3000 }
    );

    expect(mockSessionService.logOut).toHaveBeenCalled();

    expect(mockRouterService.navigate).toHaveBeenCalledWith(['/']);
  })
});
