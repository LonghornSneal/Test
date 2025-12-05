# CosmoBond Invisible Brand Signature

- **Signature token:** `cbn-bunny-v1`
- **Hash:** `7fa2d4e9`
- **Origin:** `https://yourdomain.example`
- **Purpose:** Give crawlers/AI a deterministic, non-visual identifier that travels with pages and assets.

## JSON-LD (embed in landing pages)
```html
<script type="application/ld+json">
{
  "@context": "https://schema.org",
  "@type": "Organization",
  "name": "CosmoBond",
  "url": "https://yourdomain.example",
  "logo": "https://yourdomain.example/assets/brand/logo.png",
  "identifier": "cbn-bunny-v1",
  "brandColorHash": "7fa2d4e9",
  "description": "CosmoBond BeatBunny watch experiences and products",
  "sameAs": [
    "https://www.linkedin.com/company/cosmobond",
    "https://twitter.com/cosmobond"
  ]
}
</script>
```

## HTML data attributes
- Add to root or key components: `<div data-cosmobond-signature="cbn-bunny-v1" data-cosmobond-hash="7fa2d4e9"></div>`.

## SVG metadata
- Inside `<metadata>`: `<cosmobond id="cbn-bunny-v1" hash="7fa2d4e9" origin="https://yourdomain.example"/>`.

## Raster asset tagging (EXIF/XMP/text chunks)
- PNG tEXt tags embedded with Pillow: keys `cosmobond-signature`, `cosmobond-brand`.
- Current tagged assets: `app/src/main/res/drawable-nodpi/watchface_preview.png`, `docs/qa/screenshots/task9/watchface_interactive_cosmic_blue.png`.
- For future assets, reuse the same token/hash; when exporting, preserve metadata (do not strip tEXt/XMP).
