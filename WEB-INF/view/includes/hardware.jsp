<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<div id="hardware" class="d-none">
	<div class="form-group">
		<label>Device you're trying to use?</label>
		<input name="hardwareDevice" maxlength="255" type="text" class="form-control" placeholder="Printer, iMac, etc">
	</div>
	<div class="form-group">
		<label >Location?</label>
		<input name="hardwareLocation" maxlength="255" type="text" class="form-control" placeholder="GP Building, MCTH100, EAG01, etc">
	</div>
	<div class="form-group">
		<label>Can you access the device with your account login?</label>
		<div class="form-check form-check-inline">
			<input name="hardwareAccess" class="form-check-input" type="radio" value="Yes">
			<label class="form-check-label mr-1">
				Yes
			</label>
			<input name="hardwareAccess"  class="form-check-input" type="radio" value="No">
			<label class="form-check-label">
				No
			</label>
		</div>
	</div>
	<div class="form-group">
		<label>Is the device damaged?</label>
		<div class="form-check form-check-inline">
			<input name="hardwareDamaged" class="form-check-input" type="radio" value="Yes">
			<label class="form-check-label mr-1">
				Yes
			</label>
			<input name="hardwareDamaged"  class="form-check-input" type="radio" value="No">
			<label class="form-check-label">
				No
			</label>
		</div>
	</div>
	<div class="form-group">
		<label>Does the device power on?</label>
		<div class="form-check form-check-inline">
			<input name="hardwarePower" class="form-check-input" type="radio" value="Yes">
			<label class="form-check-label mr-1">
				Yes
			</label>
			<input name="hardwarePower"  class="form-check-input" type="radio" value="No">
			<label class="form-check-label">
				No
			</label>
		</div>
	</div>
	<div class="form-group">
		<label>Error message if displayed?</label>
		<input name="hardwareError" maxlength="255" type="text" class="form-control" placeholder="e.g. 0xc05d1281">
	</div>
</div>
