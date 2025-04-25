import { Component, OnInit } from '@angular/core';
import { Hotel, Page } from '../../models/hotel.model';
import { HotelService } from '../../services/hotel.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-hotel-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './hotel-list.component.html',
  styleUrl: './hotel-list.component.css'
})
export class HotelListComponent implements OnInit {
  hotels: Hotel[] = [];
  page = 0;
  size = 10;
  totalPages = 1;
  errorMessage: string | null = null;
  keyword: string = '';
  selectedHotel: Hotel | null = null;

  constructor(private service: HotelService) {}

  ngOnInit(): void {
    this.loadHotels();
  }

  loadHotels(): void {
    this.errorMessage = null;
    this.service.getHotels(this.keyword, this.page, this.size).subscribe({
      next: (data: Page<Hotel>) => {
        this.hotels = data.content;
        this.totalPages = data.totalPages;
      },
      error: (err) => {
        this.errorMessage = err.message;
      }
    });
  }

  nextPage(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadHotels();
    }
  }

  prevPage(): void {
    if (this.page > 0) {
      this.page--;
      this.loadHotels();
    }
  }

  onSearch(): void {
    this.page = 0;
    this.loadHotels();
  }

  openDeleteModal(hotel: Hotel): void {
    this.selectedHotel = hotel;
  }

  closeDeleteModal(): void {
    this.selectedHotel = null;
  }

  confirmDeleteHotel(): void {
    if (!this.selectedHotel) return;
    this.service.deleteHotel(this.selectedHotel.id!).subscribe({
      next: () => {
        this.hotels = this.hotels.filter(h => h.id !== this.selectedHotel?.id);
        this.selectedHotel = null;
      },
      error: () => {
        this.errorMessage = 'Error deleting hotel.';
        this.selectedHotel = null;
      }
    });
  }
}
