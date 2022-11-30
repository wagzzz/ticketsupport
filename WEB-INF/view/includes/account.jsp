<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<div id="account" class="d-none">
	<div class="form-group">
		<label>Have you activated your account?</label>
		<div class="form-check form-check-inline">
			<input name="accountActivate" class="form-check-input" type="radio" value="Yes">
			<label class="form-check-label mr-1">
				Yes
			</label>
			<input name="accountActivate"  class="form-check-input mr-1" type="radio" value="No">
			<label class="form-check-label">
				No
			</label>
		</div>
	</div>
	<div class="form-group">
		<label>Can you log into a university computer?</label>
		<div class="form-check form-check-inline">
			<input name="accountUniversity" class="form-check-input" type="radio" value="Yes">
			<label class="form-check-label mr-1">
				Yes
			</label>
			<input name="accountUniversity"  class="form-check-input" type="radio" value="No">
			<label class="form-check-label">
				No
			</label>
		</div>
	</div>
	<div class="form-group">
		<label>University system you're trying to access?</label>
		<input maxlength="255" name="accountSystem" type="text" class="form-control" placeholder="e.g. NuMail, Myhub, etc">
	</div>
	<div class="form-group">
		<label>Have you tried resetting your password?</label>
		<div class="form-check form-check-inline">
			<input name="accountReset" class="form-check-input" type="radio" value="Yes">
			<label class="form-check-label mr-1">
				Yes
			</label>
			<input name="accountReset"  class="form-check-input" type="radio" value="No">
			<label class="form-check-label">
				No
			</label>
		</div>
	</div>
	<div class="form-group">
		<label>Error message if displayed?</label>
		<input maxlength="255" name="accountError" type="text" class="form-control" placeholder="e.g. Your username is not recognised Error: 3001">
	</div>
</div>
