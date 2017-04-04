const app = {

    // Constants
    DECIMAL_LOCALE: 'nl-nl',
    DECIMAL_FORMAT: '0,0.00',
    API_DATE_FORMAT: 'YYYY-MM-DD',
    MONTH_SELECTOR_FORMAT: 'YYYY-MM',
    SELECTED_MONTH_HUMAN_FORMAT: 'MMMM YYYY',
    USAGE_API_URL: 'http://localhost:8080/?from={{& from }}&to={{& to }}&path={{& path }}',
    DEFAULT_ERROR_MESSAGE: 'Unable to communicate with the Usage API. Please contact your system administrator.',

    // Templates
    domainsListTemplate: '#ui-domains-list-template',
    printingHeadersTemplate: '#ui-printing-headers-template',
    errorMessageTemplate: '#ui-error-message-template',

    // Components
    errorMessageContainer: '#ui-error-message',
    monthSelectorComponent: '#ui-month-selector',
    domainPathField: '#ui-domain-path',
    cpuPriceField: '#ui-cpu-price',
    memoryPriceField: '#ui-memory-price',
    generateReportButton: '#ui-generate-report-btn',
    printingHeadersContainer: '#ui-printing-headers',
    domainsTable: '#ui-domains-table',

    init: function() {
        numeral.locale(this.DECIMAL_LOCALE);
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

        const html = $(this.printingHeadersTemplate).html();
        const rendered = Mustache.render(html, {
            selectedMonth: selectedMonthFormatted,
            cpuPrice: cpuPriceFormatted,
            memoryPrice: memoryPriceFormatted
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

        const to = moment(selectedMonth, this.MONTH_SELECTOR_FORMAT)
            .add(1, 'months')
            .format(this.API_DATE_FORMAT);

        const path = $(this.domainPathField).val();

        const renderedUrl = Mustache.render(this.USAGE_API_URL, {
            from: from,
            to: to,
            path: path
        });

        $.get(renderedUrl, this.parseDomainsResult)
            .fail(this.parseErrorResponse);
    },

    parseDomainsResult: function(domains) {
        this.renderPrintingHeaders();
        this.calculateDomainsCosts(domains);
        this.renderDomainsList(domains);
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

        domain.cpuCost = numeral(cpuPrice.value()).multiply(domain.cpu);
        domain.memoryCost = numeral(memoryPrice.value()).multiply(domain.memory);
        domain.totalCost = numeral(domain.cpuCost.value()).add(domain.memoryCost.value());

        domain.cpu = numeral(domain.cpu).format();
        domain.memory = numeral(domain.memory).format();
        domain.cpuCost = domain.cpuCost.format();
        domain.memoryCost = domain.memoryCost.format();
        domain.totalCost = domain.totalCost.format();
    },

    renderErrorMessage: function(errorMessage) {
        const html = $(this.errorMessageTemplate).html();
        const rendered = Mustache.render(html, { errorMessage: errorMessage });
        $(this.errorMessageContainer).html(rendered);
    }

};
