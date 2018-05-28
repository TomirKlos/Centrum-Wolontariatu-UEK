import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { GenericService } from '../../shared/generic.service';
import { Category } from '../../shared/interfaces';
import { SnackBarService } from '../../shared/snack-bar.service';
import { map } from 'rxjs/operators';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { CategoryImpl } from './categories.component';

@Injectable()
export class CategoriesService extends GenericService<Category> {

  private _pageSubjectCategory = new BehaviorSubject<Category>(null);

  constructor(_http: HttpClient, _snackBar: SnackBarService) {
    super(_http, _snackBar);
    this._url = this._url + '/vrequest';
  }

  getCategories(){
      return this._http.get(this._url + "/category/");
  }

  deleteCategory(name: string){
    return this._http.delete(this._url + "/category/?name=" + name)
  }

  createCategory(categoryName: String){
    return this._http.post(this._url + '/category/', { categoryName });
  }
}
