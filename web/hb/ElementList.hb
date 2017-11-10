<div class="panel panel-default" id="ElementList">
     <div class="panel-heading">
     	  <h3 class="panel-title">All Messages</h3>
	  </div>
    <table class="table">
        <tbody>
            {{#each mData}}
         <tr>
        		<td><button type="button" class="btn btn-link" id="ElementList-viewmsg">{{this.mTitle}}</button></td>
				<td>{{this.mBody}}</td>
		{{!--		<img class="ElementList-img" src="{{http://www.wallpaperbackgrounds.org/wp-content/uploads/Picture.jpg}}" alt="image would go here"/>	--}}
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