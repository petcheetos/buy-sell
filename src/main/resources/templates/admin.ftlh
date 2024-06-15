<#import "blocks/template.ftlh" as t>
<@t.template user>
    <style>
        .table-custom {
            background-color: #abcdef !important;
            color: #000 !important;
        }
        .btn-light {
            background-color: #abcdef !important;
            color: #000 !important;
            border: 2px solid #000080 !important;
        }
        h1 {
            color: #000080;
        }
    </style>
    <h1 style="text-align: center">Панель администратора</h1>
    <hr>
    <table class="table table-hover table-custom">
        <thead>
        <tr>
            <th scope="col">Email</th>
            <th scope="col">Номер телефона</th>
            <th scope="col">Активность</th>
            <th scope="col">Роли</th>
            <th scope="col">Бан</th>
            <th scope="col">Разбан</th>
            <th scope="col">Редактирование</th>
            <th scope="col">Подробная информация</th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.email}</td>
                <td>${user.phoneNumber}</td>
                <td><#if user.active>true<#else>false</#if></td>
                <td><#list user.roles as role>${role} </#list></td>
                <td>
                    <form action="/admin/user/ban/${user.id}" method="post">
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <input type="submit" class="btn btn-light" value="Бан">
                    </form>
                </td>
                <td>
                    <form action="/admin/user/unban/${user.id}" method="post">
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <input type="submit" class="btn btn-light" value="Разбан">
                    </form>
                </td>
                <td><a href="/admin/user/edit/${user.id}">Редактирование</a></td>
                <td><a href="/user/${user.id}">Подробная информация</a></td>
            </tr>
        <#else>
            <tr>
                <td colspan="8" style="text-align: center;">Пользователей нет</td>
            </tr>
        </#list>
        </tbody>
    </table>
</@t.template>
