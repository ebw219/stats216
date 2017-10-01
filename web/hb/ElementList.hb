<div class="panel panel-default" id="ElementList">
    <div class="panel-heading">
        <h3 class="panel-title">All Data</h3>
    </div>
    <table class="table">
        <tbody>
            {{#each mData}}
            <tr>
                <td>{{this.mTitle}}</td>
                <td><button class="ElementList-editbtn" data-value="{{this.mId}}">Edit</button></td>
                <td><button class="ElementList-delbtn" data-value="{{this.mId}}">Delete</button></td>
            </tr>
            {{/each}}
        </tbody>
    </table>
</div>