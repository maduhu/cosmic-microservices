'use strict';

const app = {

    // Constants
    DECIMAL_FORMAT: '0,0.00',
    API_DATE_FORMAT: 'YYYY-MM-DD',
    MONTH_SELECTOR_FORMAT: 'YYYY-MM',
    SELECTED_MONTH_HUMAN_FORMAT: 'MMMM YYYY',
    USAGE_API_BASE_URL: undefined,
    GENERAL_USAGE_PATH: '/general?from={{& from }}&to={{& to }}&path={{& path }}',
    DEFAULT_ERROR_MESSAGE: 'Unable to communicate with the Usage API. Please contact your system administrator.',

    // Templates
    domainsListTemplate: '#ui-domains-list-template',
    printingHeadersTemplate: '#ui-printing-headers-template',
    errorMessageTemplate: '#ui-error-message-template',

    // Components
    errorMessageContainer: '#ui-error-message',
    monthSelectorComponent: '#ui-month-selector',
    untilTodayCheckbox: '#ui-month-today-checkbox',
    domainPathField: '#ui-domain-path',
    cpuPriceField: '#ui-cpu-price',
    memoryPriceField: '#ui-memory-price',
    storagePriceField: '#ui-storage-price',
    publicIpPriceField: '#ui-public-ip-price',
    generateReportButton: '#ui-generate-report-btn',
    printingHeadersContainer: '#ui-printing-headers',
    domainsTable: '#ui-domains-table',

    init: function(baseUrl) {
        this.USAGE_API_BASE_URL = baseUrl;

        numeral.defaultFormat(this.DECIMAL_FORMAT);
        _.bindAll(this, ... _.functions(this));

        $(this.monthSelectorComponent).datepicker('setDate', new Date());
        this.renderPrintingHeaders();
        this.renderDomainsList();

        $(this.generateReportButton).on('click', this.generateReportButtonOnClick);
    },

    renderPrintingHeaders: function() {
        var selectedMonth = $(this.monthSelectorComponent).datepicker('getFormattedDate');

        const selectedMonthFormatted = moment(selectedMonth, this.MONTH_SELECTOR_FORMAT)
            .format(this.SELECTED_MONTH_HUMAN_FORMAT);

        const cpuPriceFormatted = numeral($(this.cpuPriceField).val()).format();
        const memoryPriceFormatted = numeral($(this.memoryPriceField).val()).format();
        const storagePriceFormatted = numeral($(this.storagePriceField).val()).format();
        const publicIpPriceFormatted = numeral($(this.publicIpPriceField).val()).format();

        const html = $(this.printingHeadersTemplate).html();
        const rendered = Mustache.render(html, {
            selectedMonth: selectedMonthFormatted,
            cpuPrice: cpuPriceFormatted,
            memoryPrice: memoryPriceFormatted,
            storagePrice: storagePriceFormatted,
            publicIpPrice: publicIpPriceFormatted
        });
        $(this.printingHeadersContainer).html(rendered);
    },

    renderDomainsList: function(domains) {
        const html = $(this.domainsListTemplate).html();
        const rendered = Mustache.render(html, { domains: domains });
        $('tbody', this.domainsTable).html(rendered);
    },

    generateReportButtonOnClick: function(event) {
        event.preventDefault();

        const selectedMonth = $(this.monthSelectorComponent).datepicker('getFormattedDate');

        const from = moment(selectedMonth, this.MONTH_SELECTOR_FORMAT)
            .format(this.API_DATE_FORMAT);

        var to = moment(selectedMonth, this.MONTH_SELECTOR_FORMAT)
            .add(1, 'months');

        const now = moment();

        if (now.isBefore(to) && $(this.untilTodayCheckbox).prop('checked')) {
            to = now.format(this.API_DATE_FORMAT);
        } else {
            to = to.format(this.API_DATE_FORMAT);
        }

        const path = $(this.domainPathField).val();

        const renderedUrl = Mustache.render(this.USAGE_API_BASE_URL + this.GENERAL_USAGE_PATH, {
            from: from,
            to: to,
            path: path
        });

        $.get(renderedUrl, this.parseDomainsResult)
            .fail(this.parseErrorResponse);
    },

    parseDomainsResult: function(data) {
        this.renderPrintingHeaders();
        this.calculateDomainsCosts(data.domains);
        this.renderDomainsList(data.domains);
    },

    parseErrorResponse: function(response) {
        if (response.status >= 200 && response.status < 600) {
            try {
                console.log(JSON.parse(response.responseText));
            } catch (e) {
                console.log('Unable to parse error response.');
            }
        }
        this.renderErrorMessage(this.DEFAULT_ERROR_MESSAGE);
    },

    calculateDomainsCosts: function(domains) {
        _.each(domains, this.calculateDomainCosts);
    },

    calculateDomainCosts: function(domain) {
        const cpuPrice = numeral($(this.cpuPriceField).val());
        const memoryPrice = numeral($(this.memoryPriceField).val());
        const storagePrice = numeral($(this.storagePriceField).val());
        const publicIpPrice = numeral($(this.publicIpPriceField).val());

        domain.costs = {
            compute: {
                cpu: numeral(cpuPrice.value()).multiply(domain.usage.compute.total.cpu),
                memory: numeral(memoryPrice.value()).multiply(domain.usage.compute.total.memory)
            },
            storage: numeral(storagePrice.value()).multiply(domain.usage.storage.total),
            networking: {
                publicIps: numeral(publicIpPrice.value()).multiply(domain.usage.networking.total.publicIps)
            }
        };

        domain.costs.total = numeral(domain.costs.compute.cpu.value())
                                .add(domain.costs.compute.memory.value())
                                .add(domain.costs.storage.value())
                                .add(domain.costs.networking.publicIps.value());

        domain.usage.compute.total.cpu = numeral(domain.usage.compute.total.cpu).format();
        domain.usage.compute.total.memory = numeral(domain.usage.compute.total.memory).format();
        domain.usage.storage.total = numeral(domain.usage.storage.total).format();
        domain.usage.networking.total.publicIps = numeral(domain.usage.networking.total.publicIps).format();

        domain.costs.compute.cpu = domain.costs.compute.cpu.format();
        domain.costs.compute.memory = domain.costs.compute.memory.format();
        domain.costs.storage = domain.costs.storage.format();
        domain.costs.networking.publicIps = domain.costs.networking.publicIps.format();

        domain.costs.total = domain.costs.total.format();
    },

    renderErrorMessage: function(errorMessage) {
        const html = $(this.errorMessageTemplate).html();
        const rendered = Mustache.render(html, { errorMessage: errorMessage });
        $(this.errorMessageContainer).html(rendered);
    }

};
