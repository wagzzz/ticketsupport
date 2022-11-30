<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<div id="network" class="d-none">
	<div class="form-group">
		<label>Device?</label>
		<input name="networkDevice" maxlength="255" type="text" class="form-control" placeholder="Chromebook, iPhone 6, Dell Inspiron, etc">
	</div>
	<div class="form-group">
		<label>Location?</label>
		<input name="networkLocation" maxlength="255" type="text" class="form-control" placeholder="GP Building, MCTH100, EAG01, etc">
	</div>
	<div class="form-group">
		<label>Browser?</label>
		<select name="networkBrowser" class="form-control">
			<option value="Chrome">Google Chrome</option>
			<option value="Firefox">Firefox</option>
			<option value="Internet Explorer">Internet Explorer</option>
			<option value="Safari">Safari</option>
			<option value="Opera">Opera</option>
			<option value="Firefox">Microsoft Edge</option>
			<option value="UC Browswer">UC Browswer</option>
			<option value="Other">Other (Please specifiy in description)</option>
		</select>
	</div>
	<div class="form-group">
		<label>Website you're trying to connect to?</label>
		<input name="networkWebsite" maxlength="255" type="url" class="form-control" placeholder="google.com, newcastle.edu.au, etc">
	</div>

	<div class="form-group">
		<label>Are you able to access internal websites?</label>
		<div class="form-check form-check-inline">
			<input name="networkAccess" class="form-check-input" type="radio" value="Yes">
			<label class="form-check-label mr-1">
				Yes
			</label>
			<input name="networkAccess"  class="form-check-input" type="radio" value="No">
			<label class="form-check-label">
				No
			</label>
		</div>
	</div>
	<div class="form-group">
		<label>Have you tried using an alternate browser?</label>
		<div class="form-check form-check-inline">
			<input name="networkAlternate" class="form-check-input" type="radio" value="Yes">
			<label class="form-check-label mr-1">
				Yes
			</label>
			<input name="networkAlternate"  class="form-check-input" type="radio" value="No">
			<label class="form-check-label">
				No
			</label>
		</div>
	</div>
	<div class="form-group">
		<label>Have you tried restarting my device?</label>
		<div class="form-check form-check-inline">
			<input name="networkRestart" class="form-check-input" type="radio" value="Yes">
			<label class="form-check-label mr-1">
				Yes
			</label>
			<input name="networkRestart"  class="form-check-input" type="radio" value="No">
			<label class="form-check-label">
				No
			</label>
		</div>
	</div>
	<div class="form-group">
		<label>Can you access the website on another device?</label>
		<div class="form-check form-check-inline">
			<input name="networkAnotherDevice" class="form-check-input" type="radio" value="Yes">
			<label class="form-check-label mr-1">
				Yes
			</label>
			<input name="networkAnotherDevice"  class="form-check-input" type="radio" value="No">
			<label class="form-check-label">
				No
			</label>
		</div>
	</div>
</div>
