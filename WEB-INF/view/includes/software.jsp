<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<div id="software" class="d-none">
	<div class="form-group">
		<label>Device?</label>
		<input name="softwareDevice" maxlength="255" type="text" class="form-control" placeholder="Chromebook, iPhone 6, Dell Inspiron, etc">
	</div>
	<div class="form-group">
		<label>Software I'm trying to use?</label>
		<input name="softwareSoftware" maxlength="255" type="text" class="form-control" placeholder="MS Word, Adobe Acrobat, etc">
	</div>
	<div class="form-group">
		<label>Software version I'm trying to use?</label>
		<input name="softwareVersion" maxlength="255" type="text" class="form-control" placeholder="2016, CC, etc">
	</div>
	<div class="form-group">
		<label>Can you install the software?</label>
		<div class="form-check form-check-inline">
			<input name="softwareInstall" class="form-check-input" type="radio" value="Yes">
			<label class="form-check-label mr-1">
				Yes
			</label>
			<input name="softwareInstall"  class="form-check-input" type="radio" value="No">
			<label class="form-check-label">
				No
			</label>
		</div>
	</div>
	<div class="form-group">
		<label>Can you run the software?</label>
		<div class="form-check form-check-inline">
			<input name="softwareRun" class="form-check-input" type="radio" value="Yes">
			<label class="form-check-label mr-1">
				Yes
			</label>
			<input name="softwareRun"  class="form-check-input" type="radio" value="No">
			<label class="form-check-label">
				No
			</label>
		</div>
	</div>
	<div class="form-group">
		<label>Have you tried running the software on another device?</label>
		<div class="form-check form-check-inline">
			<input name="softwareAnotherDevice" class="form-check-input" type="radio" value="Yes">
			<label class="form-check-label mr-1">
				Yes
			</label>
			<input name="softwareAnotherDevice"  class="form-check-input" type="radio" value="No">
			<label class="form-check-label">
				No
			</label>
		</div>
	</div>
</div>
