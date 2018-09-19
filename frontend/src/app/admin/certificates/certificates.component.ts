import { HttpClient } from '@angular/common/http';
import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import {MatPaginator, MatSort, MatSortable} from '@angular/material';
import { tap } from 'rxjs/operators';
import { merge } from 'rxjs/observable/merge';

import { SnackBarService } from '../../shared/snack-bar.service';
import { Certificate } from '../../shared/interfaces';
import { CertificatesService } from './certificates.service';
import { GenericDataSource } from '../../shared/GenericDataSource';

@Component({
  selector: 'app-certificates',
  templateUrl: './certificates.component.html',
  styleUrls: [ './certificates.component.scss' ],
  providers: [
    CertificatesService
  ]
})
export class CertificatesComponent implements OnInit, AfterViewInit {
  certificatesData: GenericDataSource<Certificate>;

  columnsToDisplay = [ 'id', 'certified', 'user.firstName', 'user.lastName', 'email', 'volunteerRequest.id', 'volunteerRequest.user.email'];
  totalElements: number;
  pageSize = 5;

  certificates: Certificate[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild('downloadCsv') private downloadCsv: ElementRef;

  constructor(private _http: HttpClient, private _certificatesService: CertificatesService, private _sb: SnackBarService) {
  }

  ngOnInit() {
    this.sort.sort(<MatSortable>({id: 'id', start: 'desc'}));
    this.paginator.pageSize = this.pageSize;
    this.certificatesData = new GenericDataSource(this._certificatesService);
    this._loadCategoryPage();

    this._certificatesService.getPage().subscribe(d => {
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

  private _loadCategoryPage() {
    this.certificatesData.loadPage(
      { name: 'page', value: this.paginator.pageIndex },
      { name: 'size', value: this.paginator.pageSize },
      { name: 'sort', value: this.sort.active + ',' + this.sort.direction }
    );
  }

  public async getCsv(): Promise<void> {
    const blob = await this._certificatesService.downloadResource();
    const url = window.URL.createObjectURL(blob);

    const link = this.downloadCsv.nativeElement;
    link.href = url;
    link.download = 'CertyfikatyCSV.csv';
    link.click();

    window.URL.revokeObjectURL(url);
    this._loadCategoryPage();
  }

  public async downloadSingleCsv(id: number): Promise<void> {
    const blob = await this._certificatesService.downloadSingleCsv(id);
    const url = window.URL.createObjectURL(blob);

    const link = this.downloadCsv.nativeElement;
    link.href = url;
    link.download = 'CertyfikatyCSV.csv';
    link.click();

    window.URL.revokeObjectURL(url);
    this._loadCategoryPage();
  }

}

