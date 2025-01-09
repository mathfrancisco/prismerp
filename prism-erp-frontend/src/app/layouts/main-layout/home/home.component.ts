import { Component, OnInit, OnDestroy, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { CommonModule } from '@angular/common';
import {
  faRocket,
  faComments,
  faInfoCircle,
  faChevronLeft,
  faChevronRight,
  faChartLine,
  faUsers,
  faUserTie,
  faShoppingCart,
  faWarehouse,
  faFileInvoiceDollar
} from '@fortawesome/free-solid-svg-icons';

interface CarouselItem {
  icon: string;
  title: string;
  description: string;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FontAwesomeModule, CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  currentIndex = 0;
  private autoSlideInterval: any;
  private isBrowser: boolean;
  carouselItems: CarouselItem[] = [
    {
      icon: 'users',
      title: 'Cadastro',
      description: 'Cadastre usuários, clientes, fornecedores e funcionários e organize sua empresa.'
    },
    {
      icon: 'users',
      title: 'Gestão de Clientes',
      description: 'Cadastro completo, histórico de interações e gestão eficiente de documentos dos seus clientes.'
    },
    {
      icon: 'user-tie',
      title: 'Gestão de Funcionários',
      description: 'Controle completo de funcionários, cargos e departamentos em uma única plataforma.'
    },
    {
      icon: 'shopping-cart',
      title: 'Gestão de Vendas',
      description: 'Controle total do processo de vendas, desde o pedido até a entrega do produto.'
    },
    {
      icon: 'warehouse',
      title: 'Gestão de Estoque',
      description: 'Controle eficiente de entrada e saída de produtos com alertas automáticos.'
    },
    {
      icon: 'file-invoice-dollar',
      title: 'Gestão de Faturamento',
      description: 'Emissão de notas fiscais e controle financeiro integrado ao seu negócio.'
    }
  ];

  features = [
    {
      icon: 'chart-line',
      title: 'Controle financeiro',
      description: 'Cadastro de contas a pagar e a receber, com análises detalhadas e relatórios.'
    },
    {
      icon: 'users',
      title: 'Gestão de Clientes',
      description: 'Cadastro completo de clientes com histórico de interações e documentos.'
    },
    {
      icon: 'user-tie',
      title: 'Gestão de Funcionários',
      description: 'Controle completo de funcionários, cargos e departamentos em uma única plataforma.'
    },
    {
      icon: 'shopping-cart',
      title: 'Gestão de Vendas',
      description: 'Controle total do processo de vendas, desde o pedido até a entrega do produto.'
    },
    {
      icon: 'warehouse',
      title: 'Gestão de Estoque',
      description: 'Controle eficiente de entrada e saída de produtos com alertas automáticos.'
    },
    {
      icon: 'file-invoice-dollar',
      title: 'Gestão de Faturamento',
      description: 'Emissão de notas fiscais e controle financeiro integrado ao seu negócio.'
    }
  ];

  constructor(
    library: FaIconLibrary,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);

    library.addIcons(
      faRocket,
      faComments,
      faInfoCircle,
      faChevronLeft,
      faChevronRight,
      faChartLine,
      faUsers,
      faUserTie,
      faShoppingCart,
      faWarehouse,
      faFileInvoiceDollar
    );
  }

  ngOnInit(): void {
    // Only start auto-slide if in browser environment
    if (this.isBrowser) {
      this.startAutoSlide();
    }
  }

  ngOnDestroy(): void {
    if (this.isBrowser && this.autoSlideInterval) {
      clearInterval(this.autoSlideInterval);
    }
  }

  private startAutoSlide(): void {
    this.autoSlideInterval = setInterval(() => {
      this.nextSlide();
    }, 5000);
  }

  prevSlide(): void {
    if (this.isBrowser) {
      this.currentIndex = (this.currentIndex - 1 + this.carouselItems.length) % this.carouselItems.length;
    }
  }

  nextSlide(): void {
    if (this.isBrowser) {
      this.currentIndex = (this.currentIndex + 1) % this.carouselItems.length;
    }
  }
}
