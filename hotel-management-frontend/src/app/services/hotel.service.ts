import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Hotel, Page } from '../models/hotel.model';

@Injectable({
  providedIn: 'root'
})
export class HotelService {

  private baseUrl = 'http://localhost:8080/api/hotels';

  constructor(private http: HttpClient) {}

  getHotels(keyword: string = '', page: number = 0, size: number = 10): Observable<Page<Hotel>> {
    let params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<Hotel>>(`${this.baseUrl}/search`, { params });
  }

  getHotel(id: number): Observable<Hotel> {
    return this.http.get<Hotel>(`${this.baseUrl}/${id}`);
  }

  addHotel(hotel: Partial<Hotel>): Observable<Hotel> {
    return this.http.post<Hotel>(this.baseUrl, hotel);
  }

  updateHotel(id: number, hotel: Partial<Hotel>): Observable<Hotel> {
    return this.http.put<Hotel>(`${this.baseUrl}/${id}`, hotel);
  }

  deleteHotel(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
