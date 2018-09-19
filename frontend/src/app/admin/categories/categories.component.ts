import { HttpClient } from '@angular/common/http';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatSort } from '@angular/material';
import { tap } from 'rxjs/operators';
import { merge } from 'rxjs/observable/merge';

import { SnackBarService } from '../../shared/snack-bar.service';
import { GenericDataSource } from '../../shared/GenericDataSource';
import { User, Category } from '../../shared/interfaces';
import { CategoriesService } from './categories.service';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: [ './categories.component.scss' ],
  providers: [
    CategoriesService
  ]
})
export class CategoriesComponent implements OnInit, AfterViewInit {
  columnsToDisplay = [ 'name', 'delete' ];
  totalElements: number;
  pageSize = 5;
  addCategoryShow: boolean = false;

  categoryToAdd: CategoryImpl = new CategoryImpl;
  categories: Category[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private _http: HttpClient, private _categoriesService: CategoriesService, private _sb: SnackBarService) {
  }

  ngOnInit() {
    this.paginator.pageSize = this.pageSize;
    this._loadCategoryPage();

    this._categoriesService.getPage().subscribe(d => {
      if (d && d.totalElements) {
        this.totalElements = d.totalElements;
      }
    });
  }

  ngAfterViewInit() {
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this._loadCategoryPage())
      )
      .subscribe();
  }

  _loadCategoryPage(){
    this._categoriesService.getCategories()
    .subscribe((data: Category[]) => {
      this.categories = data;
    });
  }

  deleteCategory(name: string) {
    this._categoriesService.deleteCategory(name).subscribe(() => this._loadCategoryPage());
  }

  addCategory(){
    this.addCategoryShow = !this.addCategoryShow;
  }

  createCategory(): void {
   this._categoriesService.createCategory(this.categoryToAdd.categoryName)
       .subscribe( data => {
          alert("Kategoria została utworzona");
          this._loadCategoryPage();
        });
  }


}
export class CategoryImpl{
  categoryName: String;
}
