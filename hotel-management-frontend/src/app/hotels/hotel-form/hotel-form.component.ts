import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HotelService } from '../../services/hotel.service';
import { Hotel } from '../../models/hotel.model';

@Component({
  selector: 'app-hotel-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './hotel-form.component.html',
  styleUrl: './hotel-form.component.css'
})
export class HotelFormComponent implements OnInit {
  hotelForm: FormGroup;
  hotelId: number | null = null;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private service: HotelService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.hotelForm = this.fb.group({
      name: ['', Validators.required],
      address: ['', Validators.required],
      city: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.hotelId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.hotelId) {
      this.service.getHotel(this.hotelId).subscribe({
        next: hotel => this.hotelForm.patchValue(hotel),
        error: err => this.errorMessage = 'Hotel not found.'
      });
    }
  }

  onSubmit(): void {
    if (this.hotelForm.invalid) {
      this.errorMessage = 'Please fill all required fields correctly.';
      return;
    }

    const hotel: Hotel = this.hotelForm.value;
    this.errorMessage = null;
    this.successMessage = null;

    if (this.hotelId) {
      // 🔁 Mise à jour
      this.service.updateHotel(this.hotelId, hotel).subscribe({
        next: () => {
          this.successMessage = 'Hotel updated successfully!';
          setTimeout(() => this.router.navigate(['/hotels']), 2000);
        },
        error: err => this.errorMessage = err.message
      });
    } else {
      // ➕ Création
      this.service.addHotel(hotel).subscribe({
        next: () => {
          this.successMessage = 'Hotel created successfully!';
          setTimeout(() => this.router.navigate(['/hotels']), 2000);
        },
        error: err => this.errorMessage = err.message
      });
    }
  }
}
