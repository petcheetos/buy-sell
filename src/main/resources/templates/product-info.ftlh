<#import "blocks/template.ftlh" as t>
<@t.template user>
    <style>
        .carousel-control-prev-icon,
        .carousel-control-next-icon {
            background-color: black;
        }
    </style>
    <div id="myCarousel" class="carousel slide" data-ride="carousel">
        <ol class="carousel-indicators">
            <#list images as image>
                <li data-target="#myCarousel" data-slide-to="${image_index}" class="<#if image.previewImage>active</#if>"></li>
            </#list>
        </ol>
        <div class="carousel-inner">
            <#list images as image>
                <div class="carousel-item <#if image.previewImage>active</#if>">
                    <div class="bd-placeholder-img" style="width: 100%; height: 500px; background-color: #FFFFFF;">
                        <div style="width: 100%; height: 100%; display: flex; justify-content: center; align-items: center;">
                            <a href="/images/${image.id}">
                                <img class="product" style="max-height: 500px; max-width: 100%;" src="/images/${image.id}">
                            </a>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
        <a class="carousel-control-prev" href="#myCarousel" role="button" data-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <a class="carousel-control-next" href="#myCarousel" role="button" data-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>
    </div>
    <hr>
    <p><h1 style="text-align: center">${product.title} | ${product.price} ₽</h1></p>
    <p style="text-align: center">${product.description}</p>
    <p style="text-align: center">Номер телефона продавца: <b>${authorProduct.phoneNumber}</b></p>
    <hr>
    <p style="text-align: center">
        <a href="/user/${authorProduct.id}">
            <img src="/static/images/avatar.png" alt="mdo" width="32" height="32" class="rounded-circle"> ${authorProduct.name}
        </a>
    </p>
</@t.template>
