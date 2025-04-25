import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: 'hotels',
        loadChildren: () => import('./hotels/hotel.route').then(m => m.hotelRoutes)
    }
];
