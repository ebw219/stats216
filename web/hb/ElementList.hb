<div class="panel panel-default" id="ElementList">
     <div class="panel-heading">
     	  <h3 class="panel-title">All Messages</h3>
	  </div>
    <table class="table">
        <tbody>
            {{#each mData}}
         <tr>
                <td>{{this.mTitle}}</td>
				<td>{{this.mBody}}</td>
              	<td><button class="ElementList-editbtn" data-value="{{this.mId}}">Edit</button></td>
              	<td><button class="ElementList-delbtn" data-value="{{this.mId}}">Delete</button></td>
              	<td><button class="ElementList-upvotebtn" data-value="{{this.mId}}">Up Vote</button></td>
	      	<td><button class="ElementList-downvotebtn" data-value="{{this.mId}}">Down Vote</button></td>
 		<td><button class="ElementList-addcmntbtn" data-value="{{this.mId}}">Add Comment</button></td>
 		<td><div id="votes" data-value="{{this.mId}}">Votes</div></td>
	</tr>
		{{/each}}
            </tbody>
    	</table>
    </div>