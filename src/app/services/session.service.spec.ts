import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import {HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { SessionService } from './session.service';
import { User } from '../interfaces/user.interface';
import { UserService } from './user.service';

describe('SessionService', () => {
  let service: SessionService;
  let httpMock: HttpTestingController;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService],
    });

    service = TestBed.inject(SessionService);
    userService = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  })

  it('should be created', () => {
    expect(service).toBeTruthy();
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
