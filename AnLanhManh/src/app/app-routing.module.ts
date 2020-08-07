import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ProductComponent } from './product/product.component';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { AboutComponent } from './about/about.component';
import { ContacusComponent } from './contacus/contacus.component';
import { CartComponent } from './cart/cart.component';
import { OrderComponent } from './order/order.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ProfileComponent } from './profile/profile.component';
import { SetComponent } from './set/set.component';
import { SetDetailsComponent } from './set-details/set-details.component';
import { StepComponent } from './step/step.component';
import { ScheduleComponent } from './schedule/schedule.component';
import { ScheduleDetailsComponent } from './schedule-details/schedule-details.component';
import { PaymentComponent } from './payment/payment.component';
import { ShippingComponent } from './shipping/shipping.component';
import { AteComponent } from './ate/ate.component';
import { SearchComponent } from "./search/search.component";
import { SuggestComponent } from './suggest/suggest.component';
import { CompareComponent } from './compare/compare.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'pl/:page', component: ProductComponent },
  { path: 'p/:id', component: ProductDetailsComponent },
  { path: 'cl', component: SetComponent },
  { path: 'c/:id', component: SetDetailsComponent },
  { path: 'about', component: AboutComponent },
  { path: 'contact', component: ContacusComponent },
  { path: 'cart', component: CartComponent },
  { path: 'order', component: OrderComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'step', component: StepComponent },
  { path: 'order', component: OrderComponent },
  { path: 'schedule', component: ScheduleComponent },
  { path: 's/:id', component: ScheduleDetailsComponent },
  { path: 'payment', component: PaymentComponent },
  { path: 'shipping', component: ShippingComponent },
  { path: 'user/eat', component: AteComponent },
  { path: 'search', component: SearchComponent },
  { path: 'suggest', component: SuggestComponent },
  { path: 'compare', component: CompareComponent },
  { path: '**', component: HomeComponent, redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
