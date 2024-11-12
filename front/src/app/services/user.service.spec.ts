import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { User } from '../interfaces/user.interface';
import { UserService } from './user.service';
import {HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';


describe('UserService', () => {
  let userService: UserService;
  let httpMock: HttpTestingController;


  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService],
    });

    userService = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(userService).toBeTruthy();
  });

  afterEach(() => {
    httpMock.verify();
  })

  it('should be created', () => {
    expect(userService).toBeTruthy();
  });

  it('should found user by id', () => {
    let expectedUser: User = { id: 1,
                      firstName: "john",
                      lastName: "doe",
                      email: "john.doe@gmail.com",
                      password: "superpassword",
                      createdAt: new Date(),
                      admin: false};
    
    userService.getById('1').subscribe((user) => {
      expect(user).toEqual(expectedUser);
    })

    const request = httpMock.expectOne('api/user/1');
    expect(request.request.method).toBe('GET');
    
    request.flush(expectedUser);
  })

  it('should not found user because is not register', () => {
    userService.getById('1').subscribe((response) => {
      expect(response).toBeNull();
    })

    const request = httpMock.expectOne('api/user/1');
    expect(request.request.method).toBe('GET');

    request.flush(null);
  })

  it('should delete user by id', () => {

    userService.delete('1').subscribe((response) => {
      expect(response).toBeNull()
    })

    const request = httpMock.expectOne('api/user/1');
    expect(request.request.method).toBe("DELETE")

    request.flush(null);
  })
});
