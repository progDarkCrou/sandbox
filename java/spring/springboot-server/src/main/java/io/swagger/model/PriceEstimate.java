package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringBootServerCodegen", date = "2016-06-12T10:39:16.322Z")
public class PriceEstimate  {
  
  private String productId = null;
  private String currencyCode = null;
  private String displayName = null;
  private String estimate = null;
  private BigDecimal lowEstimate = null;
  private BigDecimal highEstimate = null;
  private BigDecimal surgeMultiplier = null;

  /**
   * Unique identifier representing a specific product for a given latitude & longitude. For example, uberX in San Francisco will have a different product_id than uberX in Los Angeles
   **/
  @ApiModelProperty(value = "Unique identifier representing a specific product for a given latitude & longitude. For example, uberX in San Francisco will have a different product_id than uberX in Los Angeles")
  @JsonProperty("product_id")
  public String getProductId() {
    return productId;
  }
  public void setProductId(String productId) {
    this.productId = productId;
  }

  /**
   * [ISO 4217](http://en.wikipedia.org/wiki/ISO_4217) currency code.
   **/
  @ApiModelProperty(value = "[ISO 4217](http://en.wikipedia.org/wiki/ISO_4217) currency code.")
  @JsonProperty("currency_code")
  public String getCurrencyCode() {
    return currencyCode;
  }
  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  /**
   * Display name of product.
   **/
  @ApiModelProperty(value = "Display name of product.")
  @JsonProperty("display_name")
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Formatted string of estimate in local currency of the start location. Estimate could be a range, a single number (flat rate) or \"Metered\" for TAXI.
   **/
  @ApiModelProperty(value = "Formatted string of estimate in local currency of the start location. Estimate could be a range, a single number (flat rate) or \"Metered\" for TAXI.")
  @JsonProperty("estimate")
  public String getEstimate() {
    return estimate;
  }
  public void setEstimate(String estimate) {
    this.estimate = estimate;
  }

  /**
   * Lower bound of the estimated price.
   **/
  @ApiModelProperty(value = "Lower bound of the estimated price.")
  @JsonProperty("low_estimate")
  public BigDecimal getLowEstimate() {
    return lowEstimate;
  }
  public void setLowEstimate(BigDecimal lowEstimate) {
    this.lowEstimate = lowEstimate;
  }

  /**
   * Upper bound of the estimated price.
   **/
  @ApiModelProperty(value = "Upper bound of the estimated price.")
  @JsonProperty("high_estimate")
  public BigDecimal getHighEstimate() {
    return highEstimate;
  }
  public void setHighEstimate(BigDecimal highEstimate) {
    this.highEstimate = highEstimate;
  }

  /**
   * Expected surge multiplier. Surge is active if surge_multiplier is greater than 1. Price estimate already factors in the surge multiplier.
   **/
  @ApiModelProperty(value = "Expected surge multiplier. Surge is active if surge_multiplier is greater than 1. Price estimate already factors in the surge multiplier.")
  @JsonProperty("surge_multiplier")
  public BigDecimal getSurgeMultiplier() {
    return surgeMultiplier;
  }
  public void setSurgeMultiplier(BigDecimal surgeMultiplier) {
    this.surgeMultiplier = surgeMultiplier;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceEstimate priceEstimate = (PriceEstimate) o;
    return Objects.equals(productId, priceEstimate.productId) &&
        Objects.equals(currencyCode, priceEstimate.currencyCode) &&
        Objects.equals(displayName, priceEstimate.displayName) &&
        Objects.equals(estimate, priceEstimate.estimate) &&
        Objects.equals(lowEstimate, priceEstimate.lowEstimate) &&
        Objects.equals(highEstimate, priceEstimate.highEstimate) &&
        Objects.equals(surgeMultiplier, priceEstimate.surgeMultiplier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productId, currencyCode, displayName, estimate, lowEstimate, highEstimate, surgeMultiplier);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceEstimate {\n");
    
    sb.append("  productId: ").append(productId).append("\n");
    sb.append("  currencyCode: ").append(currencyCode).append("\n");
    sb.append("  displayName: ").append(displayName).append("\n");
    sb.append("  estimate: ").append(estimate).append("\n");
    sb.append("  lowEstimate: ").append(lowEstimate).append("\n");
    sb.append("  highEstimate: ").append(highEstimate).append("\n");
    sb.append("  surgeMultiplier: ").append(surgeMultiplier).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
