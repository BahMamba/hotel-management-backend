import { Routes } from "@angular/router";
import { HotelListComponent } from "./hotel-list/hotel-list.component";
import { HotelFormComponent } from "./hotel-form/hotel-form.component";

export const hotelRoutes: Routes = [
    { path: '', component: HotelListComponent },
    { path: 'new', component: HotelFormComponent },
    { path: 'edit/:id', component: HotelFormComponent},
]